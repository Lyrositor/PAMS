package org.pcc.pams.objects;

import org.pcc.pams.math.Vector2d;

import java.awt.*;

/**
 * Represents a fan.
 *
 * The fan is located in a point of the screen and blows air in a cone area in
 * front of it, pushing bubbles away.
 */
public class Fan extends PhysicsObject {

    /**
     * The maximum permissible wind intensity.
     */
    public static final int MAX_INTENSITY = 500;
    /**
     * The fan's color (also used to calculate the wind cone color).
     */
    private final Color fanColor;
    /**
     * The wind's force vector.
     */
    private final Vector2d force;

    /**
     * Creates a new fan at the specified position, with a certain color.
     *
     * @param position The fan's position.
     * @param color    The fan's color. The cone's color will depend on this.
     */
    public Fan(Vector2d position, Color color) {
        super(position, Vector2d.NULL);

        force = Vector2d.NULL;
        fanColor = color;
    }

    /**
     * Returns the coordinates of the fan's points.
     *
     * The fan is represented as a triangle, so three points are returned.
     * @return The x and y coordinates of the fan's points.
     */
    public int[][] getFanCoordinates() {
        Vector2d[] p = new Vector2d[3];
        Vector2d[] coneVectors = getConeVectors();
        p[0] = position;
        p[1] = position.sum(coneVectors[0].getNormed(50));
        p[2] = position.sum(coneVectors[1].getNormed(50));
        return new int[][]{
                {(int) p[0].x, (int) p[1].x, (int) p[2].x, (int) p[0].x},
                {(int) p[0].y, (int) p[1].y, (int) p[2].y, (int) p[0].y},
        };
    }

    /**
     * Returns the coordinates of the wind cone's points.
     *
     * The wind cone is represented as a triangle, so three points are returned.
     * @param length The length to impose on the cone's sides' vectors.
     * @return The x and y coordinates of the wind cone's points.
     */
    public int[][] getConeCoordinates(double length) {
        Vector2d[] p = new Vector2d[3];
        Vector2d[] coneVectors = getConeVectors();
        p[0] = position;
        p[1] = position.sum(coneVectors[0].getNormed(length));
        p[2] = position.sum(coneVectors[1].getNormed(length));
        return new int[][]{
                {(int) p[0].x, (int) p[1].x, (int) p[2].x, (int) p[0].x},
                {(int) p[0].y, (int) p[1].y, (int) p[2].y, (int) p[0].y},
        };
    }

    /**
     * Returns the vectors for the wind cone's two sides.
     *
     * @return An array of two vectors representing the directions of the cone.
     */
    public Vector2d[] getConeVectors() {
        return new Vector2d[]{
                force.getAngled(force.angle() - Math.PI / 8),
                force.getAngled(force.angle() + Math.PI / 8)};
    }

    /**
     * Returns the fan's color.
     *
     * @return The fan's color.
     */
    public Color getFanColor() {
        return fanColor;
    }

    /**
     * Calculates and returns the wind cone's color.
     *
     * The wind cone has a more transparent version of the fan's color.
     * @return The wind cone's color.
     */
    public Color getConeColor() {
        return new Color(
                fanColor.getRed(), fanColor.getGreen(), fanColor.getBlue(),
                (int) (force.norm() / (2 * MAX_INTENSITY) * 255));
    }

    /**
     * Returns the intensity of the wind's force.
     *
     * @return The wind's intensity.
     */
    public double getIntensity() {
        return force.norm();
    }

    /**
     * Sets the intensity of the wind.
     *
     * @param newIntensity The new wind intensity.
     */
    public void setIntensity(double newIntensity) {
        force.setNorm(newIntensity);
    }

    /**
     * Sets the angle the wind is blowing at.
     *
     * @param newAngle The new wind's angle.
     */
    public void setAngle(double newAngle) {
        force.setAngle(newAngle);
    }

}