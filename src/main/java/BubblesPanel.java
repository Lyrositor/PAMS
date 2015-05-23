import math.Vector2d;
import objects.Bubble;
import objects.Fan;
import objects.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class BubblesPanel extends JPanel {

    private final BufferedImage image;
    private final Graphics2D buffer;
    private final PhysicsEngine physics;

    /**
     * Create the panel, configuring its buffer.
     *
     * @param physics The PhysicsEngine instance to watch.
     * @param dim     The dimensions for the panel.
     */
    public BubblesPanel(PhysicsEngine physics, int[] dim) {
        super();
        this.physics = physics;

        setSize(dim[0], dim[1]);

        image = new BufferedImage(dim[0], dim[1], BufferedImage.TYPE_INT_RGB);
        buffer = image.createGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buffer.setRenderingHints(hints);
    }

    /**
     * Redraws the panel with all of its elements.
     *
     * @param g The Graphics instance passed by Swing.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        buffer.setBackground(new Color(255, 255, 255, 0));
        buffer.clearRect(0, 0, image.getWidth(), image.getHeight());
        drawFan();
        drawBubbles();
        drawWalls();
        g.drawImage(image, 0, 0, this);
    }

    /**
     * Draws a triangular fan on screen.
     */
    private void drawFan() {
        Fan fan = physics.getFan();

        // Draw the wind's cone.
        buffer.setColor(fan.getConeColor());

        if (fan.getIntensity() > 0) {
            // Draw the wind's cone (area of effect).
            // The specified length is guaranteed to be superior to the panel's
            // size.
            buffer.setColor(fan.getConeColor());
            int[][] conePoints = fan.getConeCoordinates(
                    Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
            buffer.drawPolygon(conePoints[0], conePoints[1], conePoints[0].length);
            buffer.fillPolygon(conePoints[0], conePoints[1], conePoints[0].length);

            // Draw the fan's symbol.
            buffer.setColor(fan.getFanColor());
            int[][] fanPoints = fan.getFanCoordinates();
            buffer.drawPolygon(fanPoints[0], fanPoints[1], fanPoints[0].length);
            buffer.fillPolygon(fanPoints[0], fanPoints[1], fanPoints[0].length);
        }
    }

    /**
     * Draws every bubble on screen.
     */
    private void drawBubbles() {
        List<Bubble> bubbles = physics.getBubbles();
        for (Bubble b : bubbles) {
            Vector2d pos = b.getPosition();
            double rayon = b.getRadius();
            buffer.setColor(b.getColor());
            buffer.fillOval(
                    (int) Math.round(pos.x - rayon), (int) Math.round(pos.y - rayon),
                    (int) b.getRadius() * 2, (int) b.getRadius() * 2);
        }
    }

    /**
     * Draws every wall on screen.
     */
    private void drawWalls() {
        Wall[] walls = physics.getWalls();
        for (Wall wall : walls) {
            Vector2d pos = wall.getPosition();
            Vector2d dim = wall.getDimensions();
            buffer.setColor(wall.getColor());
            buffer.fillRect((int) pos.x, (int) pos.y, (int) dim.x, (int) dim.y);
        }
    }

}