import objects.Fan;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

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
        main.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
                main.repaint();
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
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
        BubblesSpeedListener speedListener = new BubblesSpeedListener();
        MenuFichierListener fichierListener = new MenuFichierListener();
        WindListener windListener = new WindListener();

        frame.addBubblesButton.addActionListener(nbListener);
        frame.removeBubblesButton.addActionListener(nbListener);
        frame.increaseSpeedButton.addActionListener(speedListener);
        frame.decreaseSpeedButton.addActionListener(speedListener);
        for (Component m : main.getJMenuBar().getMenu(0).getMenuComponents())
            ((JMenuItem) m).addActionListener(fichierListener);

        frame.intensitySlider.addChangeListener(windListener);
        frame.angleSlider.addChangeListener(windListener);
    }

    private void run() {
        final int dt = 10 * 1000;

        long currentTime = System.nanoTime();
        long newTime;
        long frameTime;
        long accumulator = (long) 0.0;

        while (true) {
            // Mettre à jour le temps écoulé.
            newTime = System.nanoTime();
            frameTime = Math.min(newTime - currentTime, 25 * dt);
            currentTime = newTime;
            accumulator += frameTime;

            // Actualiser la simulation physique.
            while (accumulator >= dt) {
                physics.update(dt / 1E9);
                accumulator -= dt;
            }

            // Re-dessiner les bulles.
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
                    physics.adjustSpeed(100);
                    break;
                case "-":
                    physics.adjustSpeed(-100);
                    break;
            }
        }
    }

    private class MenuFichierListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Sauvegarder":
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save As...");
                    int userSelection = fileChooser.showSaveDialog(main);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        sound.save(fileChooser.getSelectedFile());
                    }
                    break;
                case "Quitter":
                    main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
                    break;
                case "Nouveau":
                    physics.removeBubbles(physics.getBubbleCount());
                    sound.reset();
                    break;
            }
        }
    }

    private class WindListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            Fan fan = physics.getFan();
            double newIntensity = frame.intensitySlider.getValue();
            fan.setIntensity(newIntensity);
            fan.setAngle(frame.angleSlider.getValue() * Math.PI / 180);
            if (newIntensity == 0)
                frame.angleSlider.setEnabled(false);
            else
                frame.angleSlider.setEnabled(true);
        }
    }

}