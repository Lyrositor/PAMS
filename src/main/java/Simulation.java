import java.awt.image.BufferedImage;

class Simulation {

    private GraphicsEngine graphics;
    private PhysicsEngine physics;
    private SoundEngine sound;

    public Simulation(BufferedImage canvas) {
        // D�marrer les sous-syst�mes.
        physics = new PhysicsEngine();
        //graphics = new GraphicsEngine(physics, canvas);
        sound = new SoundEngine();
        sound.start();
    }

    public void run() {
        double delta = 0;
        while (true) {
            // Mettre � jour le temps �coul�.

            // Mettre � jour la simulation physique.
            physics.update(delta);

            // Mettre � jour l'affichage graphique.
            //graphics.update(delta);
        }
    }

}