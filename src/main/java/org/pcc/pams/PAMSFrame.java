package org.pcc.pams;

import javax.swing.*;

/**
 * The simulation frame, on which everything is drawn.
 * <p>
 * This is mostly a placeholder file; to generate its methods, it must be built
 * in conjunction with IntelliJ's form editor.
 */
class PAMSFrame {
    /**
     * The wind intensity slider.
     */
    public JSlider intensitySlider;

    /**
     * The button for adding bubbles (+).
     */
    public JButton addBubblesButton;

    /**
     * The button for removing bubbles (-).
     */
    public JButton removeBubblesButton;

    /**
     * The button for increasing the global bubble speed (+).
     */
    public JButton increaseSpeedButton;

    /**
     * The button for decreasing the global bubble speed (-).
     */
    public JButton decreaseSpeedButton;

    /**
     * The canvas on which to draw the bubbles.
     */
    public JPanel canvas;

    /**
     * The root of all other components.
     */
    public JPanel rootPanel;

    /**
     * The checkbox to toggle harmonic mode.
     */
    public JCheckBox harmonicCheckbox;

    /**
     * The kinetic energy's display.
     */
    public JLabel kineticEnergyLabel;

    /**
     * The label for the kinetic energy's display.
     */
    public JLabel kineticEnergyInfoLabel;

    /**
     * The combo box for selecting the fundamental note.
     */
    public JComboBox fundamentalComboBox;

    /**
     * The label for the wind's intensity slider.
     */
    private JLabel intensityLabel;

    /**
     * The label for the bubbles' global speed buttons.
     */
    private JLabel speedLabel;

    /**
     * The label for the number of bubbles' buttons.
     */
    private JLabel numberLabel;

    /**
     * The panel containing all the control elements (sliders, buttons...).
     */
    private JPanel controlPanel;

    /**
     * The panel holding the number of bubbles controls.
     */
    private JPanel numberBubblesPanel;

    /**
     * The panel holding the global bubbles' speed controls.
     */
    private JPanel speedBubblesPanel;

    /**
     * The sidebar panel.
     */
    private JPanel sidebar;

    /**
     * The label for the harmonic checkbox.
     */
    private JLabel harmonicLabel;

    /**
     * The label for the fundamental control.
     */
    private JLabel fundamentalLabel;

    /**
     * Creates a new frame with the specified panel as a canvas.
     *
     * @param panel Should be a {@link org.pcc.pams.BubblesPanel}.
     */
    public PAMSFrame(JPanel panel) {
        canvas = panel;
    }

    /**
     * Initialize certain components in a special manner.
     */
    private void createUIComponents() {
        // Small hack to force the dimensions of the window.
        canvas.setMinimumSize(canvas.getSize());
    }

}