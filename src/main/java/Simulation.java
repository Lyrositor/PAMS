import java.awt.image.BufferedImage;

class Simulation {

    private GraphicsEngine graphics;
    private PhysicsEngine physics;
    private SoundEngine sound;

    public Simulation(BufferedImage canvas) {
        // Démarrer les sous-systèmes.
        graphics = new GraphicsEngine(canvas);
        physics = new PhysicsEngine();
        sound = new SoundEngine();
        sound.start();
    }

    public void run() {
        double delta = 0;
        while (true) {
            // Mettre à jour le temps écoulé.

            // Mettre à jour la simulation physique.

            // Mettre à jour l'affichage graphique.
        }
    }

}