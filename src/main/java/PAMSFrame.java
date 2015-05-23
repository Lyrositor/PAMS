import javax.swing.*;

class PAMSFrame {
    public JTabbedPane tabbedPane;
    public JSlider angleSlider;
    public JSlider intensitySlider;
    public JButton addBubblesButton;
    public JButton removeBubblesButton;
    public JButton increaseSpeedButton;
    public JButton decreaseSpeedButton;
    public JPanel canvas;
    public JPanel rootPanel;

    public PAMSFrame(JPanel panel) {
        canvas = panel;
    }

    private void createUIComponents() {
        // Small hack to force the dimensions of the window.
        canvas.setMinimumSize(canvas.getSize());
    }


}
