package org.pcc.pams;

import org.pcc.pams.math.Vector2d;
import org.pcc.pams.objects.Fan;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Main class for the project.
 */
class PAMS {

    /**
     * The window's dimensions (width and height).
     */
    private static final int[] DIMENSIONS = {1000, 600};
    /**
     * The window's title.
     */
    private static final String TITLE = "PAMS";
    /**
     * The time step used for the physics simulation.
     */
    private static final double DT = 0.01;
    /**
     * The program's main window.
     */
    private final JFrame main;
    /**
     * A frame containing all the window's elements.
     */
    private final PAMSFrame frame;
    /**
     * The canvas on which the simulation is drawn.
     */
    private final BubblesPanel canvas;
    /**
     * An instance of the physics engine used to run the simulation.
     */
    private final PhysicsEngine physics;
    /**
     * An instance of the sound engine used to generate sound.
     */
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
        frame.fundamentalComboBox.setSelectedItem("Mi");
        setupMenu();
        setupListeners();
        main.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                main.repaint();
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
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
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem newItem = new JMenuItem("Nouveau");
        JMenuItem saveItem = new JMenuItem("Sauvegarder");
        JMenuItem quitItem = new JMenuItem("Quitter");

        newItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        saveItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        quitItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(quitItem);
        mainMenuBar.add(fileMenu);
        main.setJMenuBar(mainMenuBar);
    }

    /**
     * Attach all listeners to the desired components.
     */
    private void setupListeners() {
        BubblesNumberListener nbListener = new BubblesNumberListener();
        BubblesSpeedListener speedListener = new BubblesSpeedListener();
        MenuFileListener fileListener = new MenuFileListener();

        frame.addBubblesButton.addActionListener(nbListener);
        frame.removeBubblesButton.addActionListener(nbListener);
        frame.increaseSpeedButton.addActionListener(speedListener);
        frame.decreaseSpeedButton.addActionListener(speedListener);
        for (Component m : main.getJMenuBar().getMenu(0).getMenuComponents())
            ((JMenuItem) m).addActionListener(fileListener);

        frame.intensitySlider.addChangeListener(new WindIntensityListener());
        frame.canvas.addMouseMotionListener(new WindAngleListener());

        frame.harmonicCheckbox.addChangeListener(new HarmonicListener());
        frame.fundamentalComboBox.addItemListener(new FundamentalListener());
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
            try {
                while (accumulator >= DT) {
                    physics.update(DT);
                    accumulator -= DT;
                }
            } catch (Exception e) {
                System.out.println(
                        "ERROR: An error occurred in the physics simulation.");
                e.printStackTrace();
                System.exit(0);
            }

            // Re-draw the bubbles.
            try {
                canvas.repaint();
            } catch (Exception e) {
                System.out.println(
                        "ERROR: An error occurred while drawing.");
                e.printStackTrace();
                System.exit(0);
            }

            // Update the amount of kinetic energy.
            frame.kineticEnergyLabel.setText(
                    String.format("%.2f", physics.getTotalKineticEnergy() / 1E9));
        }
    }

    /**
     * Saves the recorded audio to file.
     */
    private void saveAudioToFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "MIDI Files", "midi");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Enregistrer sous...");
        int userSelection = fileChooser.showSaveDialog(main);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (sound.save(fileChooser.getSelectedFile()))
                JOptionPane.showMessageDialog(main,
                        "Fichier enregistré : " + selectedFile.getName(),
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(main,
                        "Erreur : impossible d'enregistrer le fichier.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Listens for events relating to the number of bubbles.
     */
    private class BubblesNumberListener implements ActionListener {
        /**
         * Called when one of the add or remove buttons is clicked.
         *
         * @param e The click event.
         */
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

    /**
     * Listens for events relating to the speed of bubbles.
     */
    private class BubblesSpeedListener implements ActionListener {
        /**
         * Called when one of the add or remove buttons is clicked.
         *
         * @param e The click event.
         */
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

    /**
     * Listens for events relating to the file menu.
     */
    private class MenuFileListener implements ActionListener {

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

    /**
     * Listens for events relating to the wind intensity slider.
     */
    private class WindIntensityListener implements ChangeListener {

        /**
         * Called when the slider's value changes.
         *
         * @param e The event to process.
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            double newIntensity = ((JSlider) e.getSource()).getValue();
            physics.getFan().setIntensity(newIntensity);
        }
    }

    /**
     * Listens for events relating to the wind angle, as modified by the mouse.
     */
    private class WindAngleListener implements MouseMotionListener {

        /**
         * Called when the user drags his mouse over the panel.
         *
         * @param e The mouse event to process.
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            Fan fan = physics.getFan();
            if (fan.getIntensity() > 0) {
                Vector2d p = fan.getPosition();
                fan.setAngle(
                        new Vector2d(e.getX() - p.x, e.getY() - p.y).angle());
            }
        }

        /**
         * Called when the user moves his mouse over the panel (ignored).
         *
         * @param e The mouse event to process.
         */
        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    /**
     * Listens for change in the value of the harmonic checkbox.
     */
    private class HarmonicListener implements ChangeListener {

        /**
         * Called when the checkbox's state is toggled.
         *
         * @param e The event to process.
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            sound.setHarmonic(((JCheckBox) e.getSource()).isSelected());
        }
    }

    /**
     * Listens for change in the value of the fundamental.
     */
    private class FundamentalListener implements ItemListener {
        /**
         * Called when the combo box's value changes.
         *
         * @param e The event to process.
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String note = (String) e.getItem();
                switch (note) {
                    case "Do":
                        sound.setFundamental(0);
                        break;
                    case "Ré":
                        sound.setFundamental(2);
                        break;
                    case "Mi":
                        sound.setFundamental(4);
                        break;
                    case "Fa":
                        sound.setFundamental(5);
                        break;
                    case "Sol":
                        sound.setFundamental(7);
                        break;
                    case "La":
                        sound.setFundamental(9);
                        break;
                    case "Si":
                        sound.setFundamental(11);
                        break;
                }
            }
        }
    }
}