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
        return new Color(
                (int) (speed.norm() / 0.44 * 255),
                (int) (speed.norm() / 0.66 * 255),
                (int) (speed.norm() / 0.88 * 255));
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return Math.PI * Math.pow(radius, 2) * DENSITY;
    }

    public void collide(Bubble bubble, Vector2d position) {
        //http://en.wikipedia.org/wiki/Elastic_collision
    }

    public Vector2d intersects(Bubble otherBubble) {
        Vector2d otherBubblePosition = otherBubble.getPosition();
        Vector2d diff = otherBubblePosition.sub(position);

        if (diff.norm() <= radius + otherBubble.getRadius())
            return diff.setNorm(radius);
        return null;
    }


    public void collide(Wall wall) {
        Vector2d linePos = wall.getBoundsLine();
        if (wall.isHorizontal()) {
            double sign = Math.signum(linePos.y - position.y);
            speed.y = -sign * Math.abs(speed.y);
        } else {
            double sign = Math.signum(linePos.x - position.x);
            speed.x = -sign * Math.abs(speed.x);
        }
    }

    public boolean intersects(Wall wall) {
        Vector2d line = wall.getBoundsLine();
        Vector2d wallPos = wall.getPosition();

        if (wall.isHorizontal() && Math.abs(line.y - position.y) <= radius) {
            return true;
        } else if (!wall.isHorizontal() && Math.abs(line.x - position.x) <= radius)
            return true;

        return false;
    }

}