import org.jfugue.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SoundEngine {

    ExecutorService soundThreadPool;
    private Player player;

    public SoundEngine() {
        player = new Player();
        soundThreadPool = Executors.newCachedThreadPool();
    }

    public void playSound(String music) {
        soundThreadPool.execute(new Sound(player, music));
    }
}

class Sound implements Runnable {

    private Player player;
    private String music;

    public Sound(Player player, String music) {
        this.player = player;
        this.music = music;
    }

    public void run() {
        player.play(music);
    }
}