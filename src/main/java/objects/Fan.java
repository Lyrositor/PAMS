package objects;

import math.Vector2d;

import java.awt.*;

/**
 * Represents a fan.
 * <p>
 * The fan is located in a point of the screen and blows air in a cone area in
 * front of it, pushing bubbles away.
 */
public class Fan extends PhysicsObject {

    public static final double MAX_INTENSITY = 10;

    private final Color fanColor;
    private final Vector2d force;

    public Fan(Vector2d position, Color color) {
        super(position, Vector2d.NULL);

        force = Vector2d.NULL;
        fanColor = color;
    }

    public int[][] getFanCoordinates() {
        Vector2d[] points = new Vector2d[3];
        points[0] = position;
        points[1] = position.sum(force.getAngled(force.angle() - Math.PI / 8).getNormed(50));
        points[2] = position.sum(force.getAngled(force.angle() + Math.PI / 8).getNormed(50));
        return new int[][]{
                {(int) points[0].x, (int) points[1].x, (int) points[2].x, (int) points[0].x},
                {(int) points[0].y, (int) points[1].y, (int) points[2].y, (int) points[0].y},
        };
    }

    public int[][] getConeCoordinates(double length) {
        Vector2d[] points = new Vector2d[3];
        Vector2d left = force.getAngled(force.angle() - Math.PI / 8);
        Vector2d right = force.getAngled(force.angle() + Math.PI / 8);
        left.setNorm(length);
        right.setNorm(length);
        points[0] = position;
        points[1] = position.sum(left);
        points[2] = position.sum(right);
        return new int[][]{
                {(int) points[0].x, (int) points[1].x, (int) points[2].x, (int) points[0].x},
                {(int) points[0].y, (int) points[1].y, (int) points[2].y, (int) points[0].y},
        };
    }

    public Color getFanColor() {
        return fanColor;
    }

    public Color getConeColor() {
        return new Color(
                fanColor.getRed(), fanColor.getGreen(), fanColor.getBlue(),
                (int) (force.norm() / (2 * MAX_INTENSITY) * 255));
    }

    public double getIntensity() {
        return force.norm();
    }

    public void setIntensity(double newIntensity) {
        force.setNorm(newIntensity);
    }

    public void setAngle(double newAngle) {
        force.setAngle(newAngle);
    }

}
