import javax.swing.*;
import java.util.Date;

class Simulation {

    private GraphicsEngine graphics;
    private PhysicsEngine physics;
    private SoundEngine sound;

    public Simulation(JPanel dessin) {
        // D�marrer les sous-syst�mes.
        physics = new PhysicsEngine();
        //graphics = new GraphicsEngine(physics, canvas);
        sound = new SoundEngine();
        sound.start();
    }

    public void run() {
        double delta;
        double previous = new Date().getTime();
        while (true) {
            // Mettre � jour le temps �coul�.
            delta = new Date().getTime() - previous;
            previous = new Date().getTime();

            // Mettre � jour la simulation physique.
            physics.update(delta);

            // Mettre � jour l'affichage graphique.
            //graphics.update(delta);
        }
    }

}