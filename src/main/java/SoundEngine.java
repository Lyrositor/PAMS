import objects.Bubble;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SoundEngine implements PhysicsListener {

    private ExecutorService soundThreadPool;
    private Synthesizer synthesizer;
    private MidiChannel[] channels;

    public SoundEngine() {
        soundThreadPool = Executors.newCachedThreadPool();

        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channels = synthesizer.getChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble) {
    }

    public void bubbleToWallCollision(Bubble bubble) {
        soundThreadPool.execute(new BubbleSound(bubble));
    }

    private class BubbleSound implements Runnable {

        private Bubble bubble;

        public BubbleSound(Bubble bubble) {
            this.bubble = bubble;
        }

        private int calcNote() {
            return (int) (127 * (1.0 - bubble.getRadius() / Bubble.MAX_RADIUS));
        }

        public void run() {
            channels[0].noteOn(calcNote(), 60);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            channels[0].noteOff(calcNote());
        }

    }
}