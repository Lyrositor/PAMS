import objects.Bubble;

import javax.sound.midi.*;
import java.io.File;

class SoundEngine implements PhysicsListener {

    private static final int NOTE_LENGTH = 32;  // Half Note
    private static final int TEMPO = 120;
    private static final int VELOCITY = 64;  // Middle Volume
    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private Track track1;

    public SoundEngine() {
        try {
            sequencer = MidiSystem.getSequencer();
            synthesizer = MidiSystem.getSynthesizer();
            reset();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize MIDI interfaces.");
            e.printStackTrace();
        }
    }

    public void reset() throws MidiUnavailableException,
            InvalidMidiDataException {
        synchronized (sequencer) {
            // Close the sequencer if it is currently running.
            if (sequencer.isRecording()) {
                sequencer.stopRecording();
                sequencer.recordDisable(track1);
            }
            if (sequencer.isOpen())
                sequencer.close();
            if (synthesizer.isOpen())
                synthesizer.close();

            // Create a new sequence and start recording on it.
            Sequence sequence = new Sequence(Sequence.PPQ, 16);
            track1 = sequence.createTrack();
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(TEMPO);
            sequencer.open();
            sequencer.recordEnable(track1, -1);
            sequencer.startRecording();
            synthesizer.open();
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
                System.err.println("ERROR: Failed to write to file.");
                e.printStackTrace();
            }
        } else
            System.err.println("ERROR: Failed to find valid MIDI file types.");
        return false;
    }

    public void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble) {
    }

    public void bubbleToWallCollision(Bubble bubble) {
        synchronized (sequencer) {
            long length = (long) ((1 - bubble.getSpeed().norm() / Bubble.MAX_SPEED) * NOTE_LENGTH);
            addNote(track1, calcNote(bubble), sequencer.getTickPosition(), length,
                    (int) (bubble.getSpeed().norm() / Bubble.MAX_SPEED * VELOCITY));
            sequencer.setTickPosition(sequencer.getTickPosition() + length);
        }
    }

    private void addNote(
            Track track, int note, long startTick, long tickLength, int velocity) {
        ShortMessage msgOn = new ShortMessage();
        ShortMessage msgOff = new ShortMessage();

        try {
            msgOn.setMessage(ShortMessage.NOTE_ON, 0, note, velocity);
            msgOff.setMessage(ShortMessage.NOTE_OFF, 0, note, velocity);
            Receiver synthesizerReceiver = synthesizer.getReceiver();
            track.add(new MidiEvent(msgOn, startTick));
            track.add(new MidiEvent(msgOff, startTick + tickLength));
            synthesizerReceiver.send(msgOn, -1);
            synthesizerReceiver.send(msgOff, -1);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to play sound.");
            e.printStackTrace();
        }
    }

    private int calcNote(Bubble bubble) {
        return (int) (127 * (1.0 - bubble.getRadius() / Bubble.MAX_RADIUS));
    }

}