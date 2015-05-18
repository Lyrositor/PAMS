package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    public static final double MAX_RADIUS = 25.0;
    public static final double MAX_SPEED = 500;
    public static final double DENSITY = 7.0;

    private double radius;

    public Bubble(double radius, Vector2d position, Vector2d speed) {
        super(position, speed);
        setSpeed(speed);
        this.radius = radius;
    }

    private static Vector2d calcCollisionSpeed(Bubble bubble, Bubble otherBubble) {
        Vector2d posDiff = bubble.getPosition().sub(otherBubble.getPosition());
        Vector2d speedDiff = bubble.getSpeed().sub(otherBubble.getSpeed());
        double m1 = bubble.getMass();
        double m2 = otherBubble.getMass();
        return bubble.getSpeed().sub(posDiff.multiply(2 * m2 / (m1 + m2) * posDiff.scalar(speedDiff) / posDiff.norm2()));
    }

    public Color getColor() {
        return new Color(
                (int) (30 + radius / 25 * 225),
                (int) (70 + radius / 25 * 185),
                (int) (110 + radius / 25 * 145));
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return Math.PI * Math.pow(radius, 2) * DENSITY;
    }

    public void setSpeed(Vector2d newSpeed) {
        super.setSpeed(newSpeed.getNormed(Math.min(newSpeed.norm(), MAX_SPEED)));
    }

    public Vector2d intersects(Bubble otherBubble) {
        Vector2d otherBubblePosition = otherBubble.getPosition();
        Vector2d diff = otherBubblePosition.sub(position);

        if (diff.norm() <= radius + otherBubble.getRadius())
            return diff.getNormed(radius);

        return null;
    }

    public void collide(Bubble otherBubble) {
        Vector2d diff = otherBubble.getPosition().sub(position);
        Vector2d newPosition = position.add(diff.getNormed(radius + otherBubble.getRadius()));
        otherBubble.setPosition(newPosition);
        speed = calcCollisionSpeed(this, otherBubble);
        otherBubble.setSpeed(calcCollisionSpeed(otherBubble, this));
        // http://en.wikipedia.org/wiki/Elastic_collision
        // http://williamecraver.wix.com/elastic-equations
        // http://www.science-calculators.org/mechanics/collisions/
    }

    public boolean intersects(Wall wall) {
        Vector2d line = wall.getBoundsLine();

        if (wall.isHorizontal() && Math.abs(line.y - position.y) <= radius) {
            return true;
        } else if (!wall.isHorizontal() && Math.abs(line.x - position.x) <= radius)
            return true;

        return false;
    }

    public void collide(Wall wall) {
        Vector2d linePos = wall.getBoundsLine();
        if (wall.isHorizontal()) {
            double sign = -Math.signum(linePos.y - position.y);
            position.y = linePos.y + sign * radius;

            speed.y = sign * Math.abs(speed.y);
        } else {
            double sign = -Math.signum(linePos.x - position.x);
            position.x = linePos.x + sign * radius;
            speed.x = sign * Math.abs(speed.x);
        }
    }
}