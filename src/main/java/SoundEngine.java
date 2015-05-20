import objects.Bubble;

import javax.sound.midi.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SoundEngine implements PhysicsListener {

    private ExecutorService soundThreadPool;
    private Sequencer sequencer;
    private Synthesizer synthesizer;

    public SoundEngine() {
        soundThreadPool = Executors.newCachedThreadPool();

        try {
            sequencer = MidiSystem.getSequencer();
            Transmitter transmitter = sequencer.getTransmitter();
            synthesizer = MidiSystem.getSynthesizer();
            Receiver receiver = sequencer.getReceiver();
            transmitter.setReceiver(receiver);

            Sequence sequence = new Sequence(Sequence.PPQ, 1);
            sequence.createTrack();
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(120);

            sequencer.open();
            sequencer.startRecording();
            synthesizer.open();
        } catch (Exception e) {
            System.out.println("ERROR: Failed to initialize MIDI interfaces.");
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            synthesizer.close();
            sequencer.stopRecording();
            sequencer.close();
            synthesizer.open();
            sequencer.open();
            sequencer.startRecording();
        } catch (Exception e) {
            System.out.println("ERROR: Failed to re-initialize MIDI interfaces.");
            e.printStackTrace();
        }
    }

    public boolean save(File file) {
        Sequence sequence = sequencer.getSequence();
        int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
        if (fileTypes.length > 0) {
            try {
                MidiSystem.write(sequence, fileTypes[0], file);
                return true;
            } catch (Exception e) {
                System.out.println("ERROR: Failed to write to file.");
                e.printStackTrace();
            }
        } else
            System.out.println("ERROR: Failed to find valid MIDI file types.");
        return false;
    }

    public void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble) {
    }

    public void bubbleToWallCollision(Bubble bubble) {
        soundThreadPool.execute(new BubbleSound(bubble));
    }

    private class BubbleSound implements Runnable {

        private Bubble bubble;

        private int TICK = 16;

        public BubbleSound(Bubble bubble) {
            this.bubble = bubble;
        }

        private int calcNote() {
            return (int) (127 * (1.0 - bubble.getRadius() / Bubble.MAX_RADIUS));
        }

        public void run() {
            ShortMessage msgOn = new ShortMessage();
            ShortMessage msgOff = new ShortMessage();
            Track track = sequencer.getSequence().getTracks()[0];
            try {
                msgOn.setMessage(ShortMessage.NOTE_ON, 0, calcNote(), 100);
                msgOff.setMessage(ShortMessage.NOTE_OFF, 0, calcNote(), 100);

                track.add(new MidiEvent(msgOn, 0));
                track.add(new MidiEvent(msgOff, 4));
            } catch (Exception e) {
                System.out.println("ERROR: Failed to play sound.");
                e.printStackTrace();
            }
        }

    }
}