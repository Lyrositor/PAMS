package objects;

import math.Vector2d;

import java.awt.*;

/**
 * Represents a floating bubble.
 */
public class Bubble extends PhysicsObject {

    public static final double MAX_RADIUS = 25.0;
    public static final double MAX_SPEED = 500;
    private static final double DENSITY = 7.0;
    private final double radius;

    /**
     * Creates a new bubble.
     *
     * @param radius   The bubble's radius.
     * @param position The bubble's initial position.
     * @param speed    The bubble's initial speed.
     */
    public Bubble(double radius, Vector2d position, Vector2d speed) {
        super(position, speed);
        setSpeed(speed);
        this.radius = radius;
    }

    /**
     * Calculates the resultant speed from a collision with another bubble.
     *
     * @param bubble The first colliding bubble, to calculate the speed for.
     * @param otherBubble The second colliding bubble.
     * @return Returns the new speed for bubble.
     */
    private static Vector2d calcCollisionSpeed(Bubble bubble, Bubble otherBubble) {
        Vector2d posDiff = bubble.getPosition().sub(otherBubble.getPosition());
        Vector2d speedDiff = bubble.getSpeed().sub(otherBubble.getSpeed());
        double m1 = bubble.getMass();
        double m2 = otherBubble.getMass();
        return bubble.getSpeed().sub(posDiff.product(2 * m2 / (m1 + m2) * posDiff.scalar(speedDiff) / posDiff.norm2()));
    }

    /**
     * Calculates the color of the bubble based on its size and returns it.
     *
     * @return The color of the bubble.
     */
    public Color getColor() {
        return new Color(
                (int) (30 + radius / 25 * 225),
                (int) (70 + radius / 25 * 185),
                (int) (110 + radius / 25 * 145));
    }

    /**
     * Returns the radius of the bubble.
     *
     * @return The bubble's radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Calculates and returns the bubble's mass.
     *
     * The "mass" is used in physics collision calculations.
     * @return The bubble's mass.
     */
    public double getMass() {
        return Math.PI * Math.pow(radius, 2) * DENSITY;
    }

    /**
     * Changes the bubble's speed, ensuring it is smaller than the max speed.
     *
     * @param newSpeed The new speed to apply.
     */
    @Override
    public void setSpeed(Vector2d newSpeed) {
        super.setSpeed(newSpeed.getNormed(Math.min(newSpeed.norm(), MAX_SPEED)));
    }

    /**
     * Checks if the bubble intersects with a specified "virtual" bubble.
     *
     * @param otherOrigin The virtual bubble's origin.
     * @param otherRadius The virtual bubble's radius.
     * @return True if it intersects, false otherwise.
     */
    public boolean intersects(Vector2d otherOrigin, double otherRadius) {
        Vector2d diff = otherOrigin.sub(position);
        return diff.norm() <= radius + otherRadius;
    }

    /**
     * Checks if the bubble intersects with the fan's cone of wind.
     * <p>
     * To simplify calculations, we only check if the center of the circle is in
     * the triangle, since this is where the force gets applied. We do not care
     * about the cone's length either, since we are assuming it has infinite
     * reach.
     *
     * @param fan The fan to check against.
     * @return True if it intersects, false otherwise.
     */
    public boolean intersects(Fan fan) {
        double angle = position.sub(fan.getPosition()).angle();
        Vector2d[] coneVectors = fan.getConeVectors();
        return angle > coneVectors[0].angle() && angle < coneVectors[1].angle();
    }

    /**
     * Checks if the bubble intersects with another bubble.
     *
     * @param otherBubble The other bubble to check for intersection.
     * @return True if they intersect, false otherwise.
     */
    public boolean intersects(Bubble otherBubble) {
        Vector2d otherBubblePosition = otherBubble.getPosition();
        Vector2d diff = otherBubblePosition.sub(position);
        return diff.norm() <= radius + otherBubble.getRadius();
    }

    /**
     * Checks if the bubble intersects with the wall.
     *
     * @param wall The wall to check.
     * @return True if they intersect, false otherwise.
     */
    public boolean intersects(Wall wall) {
        Vector2d line = wall.getBoundsLine();

        if (wall.isHorizontal() && Math.abs(line.y - position.y) <= radius) {
            return true;
        } else if (!wall.isHorizontal() && Math.abs(line.x - position.x) <= radius)
            return true;

        return false;
    }

    /**
     * Applies a collision to this bubble and the colliding bubble.
     *
     * @param otherBubble The other bubble to apply the collision to.
     */
    public void collide(Bubble otherBubble) {
        Vector2d diff = otherBubble.getPosition().sub(position);
        Vector2d newPosition = position.sum(diff.getNormed(radius + otherBubble.getRadius()));
        otherBubble.setPosition(newPosition);
        setSpeed(calcCollisionSpeed(this, otherBubble));
        otherBubble.setSpeed(calcCollisionSpeed(otherBubble, this));
    }

    /**
     * Apply collision effects to this bubble.
     *
     * @param wall The wall this bubble is colliding with.
     */
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

    /**
     * Returns a string representation of the bubble.
     *
     * @return The bubble's representation.
     */
    @Override
    public String toString() {
        return String.format(
                "Bubble(position=%s, speed=%s, radius=%s)",
                position, speed, radius);
    }
}