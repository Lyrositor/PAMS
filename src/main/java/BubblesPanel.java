import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class BubblesPanel extends JPanel {

    private Graphics2D buffer;
    private BufferedImage arrierePlan;
    private Image wall;

    private PhysicsEngine physics;

    public BubblesPanel(PhysicsEngine physics, int[] dim) {
        super();
        this.physics = physics;

        setSize(dim[0], dim[1]);

        arrierePlan = new BufferedImage(dim[0], dim[1], BufferedImage.TYPE_INT_RGB);
        buffer = arrierePlan.createGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buffer.setRenderingHints(hints);
    }

    public void paint(Graphics g) {
        super.paint(g);

        List<Bubble> bubbles = physics.getBubbles();
        Wall[] walls = physics.getWalls();

        // Dessine toutes les bulles dans le buffer.
        buffer.setBackground(new Color(255, 255, 255, 0));
        buffer.clearRect(0, 0, arrierePlan.getWidth(), arrierePlan.getHeight());
        for (Bubble b : bubbles) {
            Vector2d pos = b.getPosition();
            double rayon = b.getRadius();
            buffer.setColor(b.getColor());
            buffer.fillOval(
                    (int) Math.round(pos.x - rayon), (int) Math.round(pos.y - rayon),
                    (int) b.getRadius() * 2, (int) b.getRadius() * 2);
        }

        // Dessine tous les murs dans le buffer.
        for (Wall wall : walls) {
            Vector2d pos = wall.getPosition();
            Vector2d dim = wall.getDimensions();
            buffer.setColor(wall.getColor());
            buffer.fillRect((int) pos.x, (int) pos.y, (int) dim.x, (int) dim.y);
        }

        g.drawImage(arrierePlan, 0, 0, this);
    }

}