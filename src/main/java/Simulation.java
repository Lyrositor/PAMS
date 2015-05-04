import javax.swing.*;
import java.util.Date;

class Simulation {

    private GraphicsEngine graphics;
    private PhysicsEngine physics;
    private SoundEngine sound;

    public Simulation(JPanel dessin) {
        // Démarrer les sous-systèmes.
        physics = new PhysicsEngine();
        //graphics = new GraphicsEngine(physics, canvas);
        sound = new SoundEngine();
        sound.start();
    }

    public void run() {
        double delta;
        double previous = new Date().getTime();
        while (true) {
            // Mettre à jour le temps écoulé.
            delta = new Date().getTime() - previous;
            previous = new Date().getTime();

            // Mettre à jour la simulation physique.
            physics.update(delta);

            // Mettre à jour l'affichage graphique.
            //graphics.update(delta);
        }
    }

}