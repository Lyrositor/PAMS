import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

class PAMS {

    private static final int[] DIMENSIONS = {640, 480};
    private static final String TITLE = "PAMS";

    private JFrame main;
    private PAMSFrame frame;
    private PhysicsEngine physics;
    private SoundEngine sound;
    private BubblesPanel canvas;

    private PAMS() {
        // Initialiser les sous-systèmes.
        physics = new PhysicsEngine(DIMENSIONS);
        sound = new SoundEngine();
        physics.addListener(sound);
        canvas = new BubblesPanel(physics, DIMENSIONS);

        // Choisir l'apparence par défaut du système d'exploitation.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("ERROR: Failed to set system look and feel.");
        }

        // Créer la fenêtre.
        main = new JFrame(TITLE);
        frame = new PAMSFrame(canvas);
        main.setResizable(false);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setContentPane(frame.rootPanel);
        setupMenu();
        setupListeners();
        main.pack();
        main.setVisible(true);
    }

    public static void main(String[] args) {
        PAMS app = new PAMS();
        app.run();
    }

    private void setupMenu() {
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu fichierMenu = new JMenu("Fichier");
        JMenuItem nouveau = new JMenuItem("Nouveau");
        JMenuItem quitter = new JMenuItem("Quitter");
        JMenuItem sauvegarder = new JMenuItem("Sauvegarder");

        fichierMenu.add(nouveau);
        fichierMenu.add(sauvegarder);
        fichierMenu.add(quitter);
        mainMenuBar.add(fichierMenu);
        main.setJMenuBar(mainMenuBar);
    }

    private void setupListeners() {
        BubblesNumberListener nbListener = new BubblesNumberListener();
        frame.addBubblesButton.addActionListener(nbListener);
        frame.removeBubblesButton.addActionListener(nbListener);
    }

    private void run() {
        double delta;
        double previous = new Date().getTime();

        while (true) {
            // Mettre à jour le temps écoulé.
            delta = new Date().getTime() - previous;
            previous = new Date().getTime();

            // Mettre à jour la simulation physique.
            physics.update(delta);

            // Mettre à jour le canvas.
            canvas.repaint();
        }
    }

    private class BubblesNumberListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "+":
                    physics.addBubbles(1);
                    break;
                case "-":
                    physics.removeBubbles(1);
                    break;
            }
        }
    }

    private class BubblesSpeedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "+":
                    break;
                case "-":
                    break;
            }
        }
    }
}