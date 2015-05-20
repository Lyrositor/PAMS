import math.Vector2d;
import objects.Bubble;
import objects.Fan;
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

        arrierePlan = new BufferedImage(dim[0], dim[1], BufferedImage.TYPE_INT_RGB);
        buffer = arrierePlan.createGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buffer.setRenderingHints(hints);
    }

    /**
     * Redraws the panel with all of its elements.
     * @param g The Graphics instance passed by Swing.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        buffer.setBackground(new Color(255, 255, 255, 0));
        buffer.clearRect(0, 0, arrierePlan.getWidth(), arrierePlan.getHeight());
        drawFan();
        drawBubbles();
        drawWalls();
        g.drawImage(arrierePlan, 0, 0, this);
    }

    /**
     * Draws a triangular fan on screen.
     */
    public void drawFan() {
        Fan fan = physics.getFan();

        // Draw the wind's cone.
        buffer.setColor(fan.getConeColor());

        if (fan.getIntensity() > 0) {
            // Draw the wind's cone (area of effect).
            // The specified length is guaranteed to be superior to the panel's
            // size.
            buffer.setColor(fan.getConeColor());
            int[][] coneCoords = fan.getConeCoordinates(
                    Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
            buffer.drawPolygon(coneCoords[0], coneCoords[1], coneCoords[0].length);
            buffer.fillPolygon(coneCoords[0], coneCoords[1], coneCoords[0].length);

            // Draw the fan's symbol.
            buffer.setColor(fan.getFanColor());
            int[][] fanCoords = fan.getFanCoordinates();
            buffer.drawPolygon(fanCoords[0], fanCoords[1], fanCoords[0].length);
            buffer.fillPolygon(fanCoords[0], fanCoords[1], fanCoords[0].length);
        }
    }

    /**
     * Draws every bubble on screen.
     */
    public void drawBubbles() {
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
    public void drawWalls() {
        Wall[] walls = physics.getWalls();
        for (Wall wall : walls) {
            Vector2d pos = wall.getPosition();
            Vector2d dim = wall.getDimensions();
            buffer.setColor(wall.getColor());
            buffer.fillRect((int) pos.x, (int) pos.y, (int) dim.x, (int) dim.y);
        }
    }

}