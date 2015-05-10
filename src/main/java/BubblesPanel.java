import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class BubblesPanel extends JPanel {

    private Graphics buffer;
    private BufferedImage arrierePlan;
    private Image wall;

    private PhysicsEngine physics;

    public BubblesPanel(PhysicsEngine physics, int width, int height) {
        super();
        this.physics = physics;

        setSize(width, height);

        arrierePlan = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffer = arrierePlan.getGraphics();

        Toolkit T = Toolkit.getDefaultToolkit();
        wall = T.getImage("room.jpg");
    }

    public void paint(Graphics g) {
        super.paint(g);
        ArrayList<Bubble> bubbles = physics.getBubbles();
        Wall[] walls = physics.getWalls();

        g.drawImage(arrierePlan, 0, 0, this);
        buffer.drawImage(wall, 0, 0, this);

        // Dessine toutes les bulles dans le buffer.
        for (int k = 0; k < bubbles.size(); k++) {
            Bubble b = bubbles.get(k);
            Vector2d pos = b.getPosition();
            double rayon = b.getRadius();
            g.setColor(b.getColor());
            g.fillOval(
                    (int) Math.round(pos.x - rayon), (int) Math.round(pos.y - rayon),
                    (int) b.getRadius() * 2, (int) b.getRadius() * 2);
        }

        // Dessine tous les murs dans le buffer.
        for (int k = 0; k < walls.length; k++) {
            Wall w = walls[k];
            Vector2d pos = w.getPosition();
            Vector2d dim = w.getDimensions();
            g.setColor(Color.blue);
            g.fillRect((int) pos.x, (int) pos.y, (int) dim.x, (int) dim.y);
        }
    }

}