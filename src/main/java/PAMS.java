import math.Vector2d;
import objects.Fan;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

class PAMS {

    private static final int[] DIMENSIONS = {1000, 600};
    private static final String TITLE = "PAMS";
    private static final double DT = 0.01;

    private final JFrame main;
    private final PAMSFrame frame;
    private final PhysicsEngine physics;
    private final BubblesPanel canvas;
    private SoundEngine sound;

    /**
     * Initialize all program components and show the window.
     */
    private PAMS() {
        // Initialise the sub-systems.
        physics = new PhysicsEngine(DIMENSIONS);
        try {
            sound = new SoundEngine();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize MIDI interfaces.");
            e.printStackTrace();
            System.exit(0);
        }
        physics.addListener(sound);
        canvas = new BubblesPanel(physics, DIMENSIONS);

        // Choose the default appearance for the OS.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to set system look and feel.");
        }

        // Create the window.
        main = new JFrame(TITLE);
        frame = new PAMSFrame(canvas);
        main.setResizable(false);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setContentPane(frame.rootPanel);
        frame.intensitySlider.setMaximum(Fan.MAX_INTENSITY);
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

    /**
     * Launches the program and runs it.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        PAMS app = new PAMS();
        app.run();
    }

    /**
     * Create the main menu toolbar.
     */
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

    /**
     * Attach all listeners to the desired components.
     */
    private void setupListeners() {
        BubblesNumberListener nbListener = new BubblesNumberListener();
        BubblesSpeedListener speedListener = new BubblesSpeedListener();
        MenuFichierListener fichierListener = new MenuFichierListener();

        frame.addBubblesButton.addActionListener(nbListener);
        frame.removeBubblesButton.addActionListener(nbListener);
        frame.increaseSpeedButton.addActionListener(speedListener);
        frame.decreaseSpeedButton.addActionListener(speedListener);
        for (Component m : main.getJMenuBar().getMenu(0).getMenuComponents())
            ((JMenuItem) m).addActionListener(fichierListener);

        frame.intensitySlider.addChangeListener(new WindIntensityListener());

        frame.canvas.addMouseMotionListener(new WindAngleListener());
    }

    /**
     * Run the simulation forever.
     *
     * This uses a fixed time step for simulating, and a variable time step for
     * drawing.
     */
    private void run() {
        double currentTime = System.nanoTime() / 1E9;
        double newTime;
        double frameTime;
        double accumulator = 0;

        while (true) {
            // Update the elapsed time.
            newTime = System.nanoTime() / 1E9;
            frameTime = Math.min(newTime - currentTime, 25 * DT);
            currentTime = newTime;
            accumulator += frameTime;

            // Update the physics simulation.
            while (accumulator >= DT) {
                physics.update(DT);
                accumulator -= DT;
            }

            // Re-draw the bubbles.
            canvas.repaint();
        }
    }

    /**
     * Saves the recorded audio to file.
     */
    private void saveAudioToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer sous...");
        int userSelection = fileChooser.showSaveDialog(main);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            if (sound.save(fileChooser.getSelectedFile()))
                JOptionPane.showMessageDialog(main,
                        "Fichier enregistré : " + fileChooser.getSelectedFile().getName(),
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(main,
                        "Erreur : impossible d'enregistrer le fichier.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    private class BubblesNumberListener implements ActionListener {
        @Override
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
        @Override
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

        /**
         * Processes an menu click event.
         *
         * @param e The event to process.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Sauvegarder":
                    saveAudioToFile();
                    break;
                case "Quitter":
                    main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
                    break;
                case "Nouveau":
                    physics.removeBubbles(physics.getBubbleCount());
                    try {
                        sound.reset();
                    } catch (Exception ex) {
                        System.err.println("ERROR: Failed to reset sound.");
                        ex.printStackTrace();
                    }
                    physics.addBubbles(PhysicsEngine.NUM_INITIAL_BUBBLES);
                    break;
            }
        }
    }

    private class WindIntensityListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            double newIntensity = frame.intensitySlider.getValue();
            physics.getFan().setIntensity(newIntensity);
        }
    }

    private class WindAngleListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            Fan fan = physics.getFan();
            if (fan.getIntensity() > 0) {
                Vector2d p = fan.getPosition();
                fan.setAngle(new Vector2d(e.getX() - p.x, e.getY() - p.y).angle());
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}