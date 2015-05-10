package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    private static final double DENSITY = 7.0;
    private double radius;

    public Bubble(double radius, Vector2d position, Vector2d speed) {
        super(position, speed);
        this.radius = radius;
    }

    public Color getColor() {
        return Color.red;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return Math.PI * Math.pow(radius, 2) * DENSITY;
    }

    public Vector2d intersects(Bubble otherBubble) {
        Vector2d otherBubblePosition = otherBubble.getPosition();
        Vector2d diff = otherBubblePosition.sub(position);

        if (diff.norm() <= radius + otherBubble.getRadius())
            return diff.setNorm(radius);
        return null;
    }

    public boolean intersects(Wall wall) {
        Vector2d line = wall.getBoundsLine();
        Vector2d wallPos = wall.getPosition();
        Vector2d diff = wallPos.sub(position);

        if (wall.isHorizontal() && Math.abs(line.y - position.y) <= radius) {
            return true;
        } else if (!wall.isHorizontal() && Math.abs(line.x - position.x) <= radius)
            return true;

        return false;
    }

}