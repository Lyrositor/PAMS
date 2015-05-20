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
        // Petit hack pour forcer les dimensions de la fen�tre.
        canvas.setMinimumSize(canvas.getSize());
    }


}
