import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class BubblesPanel extends JPanel {

    private Graphics buffer;
    private BufferedImage arrierePlan;
    private Image wall;

    private PhysicsEngine physics;

    public BubblesPanel(PhysicsEngine physics, int[] dim) {
        super();
        this.physics = physics;

        setSize(dim[0], dim[1]);

        arrierePlan = new BufferedImage(dim[0], dim[1], BufferedImage.TYPE_INT_RGB);
        buffer = arrierePlan.getGraphics();

        Toolkit T = Toolkit.getDefaultToolkit();
    }

    public void paint(Graphics g) {
        super.paint(g);

        // Configurer l'outil de dessin.
        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);

        List<Bubble> bubbles = physics.getBubbles();
        Wall[] walls = physics.getWalls();

        g2d.drawImage(arrierePlan, 0, 0, this);

        // Dessine toutes les bulles dans le buffer.
        for (int k = 0; k < bubbles.size(); k++) {
            Bubble b = bubbles.get(k);
            Vector2d pos = b.getPosition();
            double rayon = b.getRadius();
            g2d.setColor(b.getColor());
            g2d.fillOval(
                    (int) Math.round(pos.x - rayon), (int) Math.round(pos.y - rayon),
                    (int) b.getRadius() * 2, (int) b.getRadius() * 2);
        }

        // Dessine tous les murs dans le buffer.
        for (int k = 0; k < walls.length; k++) {
            Wall w = walls[k];
            Vector2d pos = w.getPosition();
            Vector2d dim = w.getDimensions();
            g2d.setColor(w.getColor());
            g2d.fillRect((int) pos.x, (int) pos.y, (int) dim.x, (int) dim.y);
        }

        g2d.dispose();
    }

}