import org.jfugue.player.Player;

class SoundEngine extends Thread {

    private Player player;

    public SoundEngine() {
        super();
        player = new Player();
    }

    public void run() {
        player.play("E E F G G F E D C C D E D D");
    }
}