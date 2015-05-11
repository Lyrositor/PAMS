import org.jfugue.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SoundEngine implements PhysicsListener {

    ExecutorService soundThreadPool;
    private Player player;

    public SoundEngine() {
        player = new Player();
        Sound.player = player;
        soundThreadPool = Executors.newCachedThreadPool();
    }

    public void playSound(String music) {
        soundThreadPool.execute(new Sound(music));
    }

    public void bubbleToBubbleCollision() {
        //playSound("B");
    }

    public void bubbleToWallCollision() {
        playSound("E");
    }
}

class Sound implements Runnable {

    public static Player player;
    private String music;

    public Sound(String music) {
        this.music = music;
    }

    public void run() {
        player.play(music);
    }
}