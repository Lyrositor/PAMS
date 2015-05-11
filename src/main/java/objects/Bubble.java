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

    private static Vector2d calcCollisionSpeed(Bubble bubble, Bubble otherBubble) {
        double phi = Math.PI;
        double theta1 = bubble.getSpeed().angle();
        double theta2 = otherBubble.getSpeed().angle();
        double v1 = bubble.getSpeed().norm();
        double v2 = otherBubble.getSpeed().norm();
        double m1 = bubble.getMass();
        double m2 = otherBubble.getMass();
        double common1 = (v1 * Math.cos(theta1 - phi) * (m1 - m2) + 2 * m2 * v2 * Math.cos(theta2 - phi)) / (m1 + m2);
        double common2 = v1 * Math.sin(theta1 - phi);
        return new Vector2d(
                common1 * Math.cos(phi) + common2 * Math.cos(phi + Math.PI / 2),
                common1 * Math.sin(phi) + common2 * Math.sin(phi + Math.PI / 2));
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

    public void collide(Bubble otherBubble, Vector2d position) {
        speed = calcCollisionSpeed(this, otherBubble);
        otherBubble.setSpeed(calcCollisionSpeed(otherBubble, this));
        //http://en.wikipedia.org/wiki/Elastic_collision
    }

    // http://williamecraver.wix.com/elastic-equations

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