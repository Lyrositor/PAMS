import javax.swing.*;

/**
 * The simulation frame, on which everything is drawn.
 * <p>
 * This is mostly a placeholder file; to generate its methods, it must be built
 * in conjunction with IntelliJ's form editor.
 */
class PAMSFrame {
    public JSlider intensitySlider;
    public JButton addBubblesButton;
    public JButton removeBubblesButton;
    public JButton increaseSpeedButton;
    public JButton decreaseSpeedButton;
    public JPanel canvas;
    public JPanel rootPanel;
    public JCheckBox harmonicCheckbox;
    public JLabel kineticEnergyLabel;
    public JLabel kineticEnergyInfoLabel;
    public JComboBox fundamentalComboBox;

    private JLabel intensityLabel;
    private JLabel speedLabel;
    private JLabel numberLabel;
    private JPanel controlPanel;
    private JPanel numberBubblesPanel;
    private JPanel speedBubblesPanel;
    private JPanel sidebar;
    private JLabel harmonicLabel;
    private JLabel fundamentalLabel;

    public PAMSFrame(JPanel panel) {
        canvas = panel;
    }

    private void createUIComponents() {
        // Small hack to force the dimensions of the window.
        canvas.setMinimumSize(canvas.getSize());
    }

}