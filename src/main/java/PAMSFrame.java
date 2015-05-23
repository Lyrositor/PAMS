import javax.swing.*;

class PAMSFrame {
    public JSlider intensitySlider;
    public JButton addBubblesButton;
    public JButton removeBubblesButton;
    public JButton increaseSpeedButton;
    public JButton decreaseSpeedButton;
    public JPanel canvas;
    public JPanel rootPanel;
    private JLabel intensityLabel;
    private JLabel speedLabel;
    private JLabel numberLabel;
    private JPanel controlPanel;
    private JPanel numberBubblesPanel;
    private JPanel speedBubblesPanel;
    private JPanel sidebar;

    public PAMSFrame(JPanel panel) {
        canvas = panel;
    }

    private void createUIComponents() {
        // Small hack to force the dimensions of the window.
        canvas.setMinimumSize(canvas.getSize());
    }

}
