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

    public void bubbleToBubbleCollision() {
    }

    public void bubbleToWallCollision() {
        soundThreadPool.execute(new Sound());
    }

    private class Sound implements Runnable {

        public void run() {
            channels[0].noteOn(60, 60);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            channels[0].noteOff(60);
        }

    }
}