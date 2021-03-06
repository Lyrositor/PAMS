package org.pcc.pams.objects;

import org.pcc.pams.math.Vector2d;

import java.awt.*;

/**
 * Represents a static wall.
 *
 * The wall is defined by two objects: a rectangle, which describes its
 * appearance, and an infinite line, which be horizontal or vertical, which
 * represents the boundary for the wall.
 */
public class Wall extends PhysicsObject {

    /**
     * The collision intensity incrementing unit.
     * <p>
     * Raise this to make the intensity rise faster.
     */
    public static final double INTENSITY_INC = 10;
    /**
     * The collision intensity decrementing unit.
     *
     * Lower this to make the intensity diminish faster.
     */
    public static final double INTENSITY_DEC = -0.3;
    /**
     * The wall's drawable dimensions.
     */
    private final Vector2d dimensions;
    /**
     * A point of the infinite boundary line's position.
     *
     * Used to calculate collisions.
     */
    private final Vector2d boundsLine;
    /**
     * The wall's base color, from which its current color is calculated.
     *
     * The wall's color depends on its {@code collisionIntensity}.
     */
    private final Color baseColor;
    /**
     * Whether the wall is horizontal or vertical.
     */
    private final boolean isHorizontal;
    /**
     * The wall's current level of collision.
     *
     * This represents how recently and how intensely the wall has been
     * collided with, and is used to modify its color.
     */
    private double collisionIntensity = 0;

    /**
     * Constructs a new static wall.
     *
     * @param dimensions The width and height of the wall's representation.
     * @param position The position of the top-left corner of the wall.
     * @param isHorizontal Whether the wall is horizontal or vertical.
     * @param boundsOffset The distance along one coordinate between the wall's
     *                     position and its bounds.
     * @param baseColor The wall's base color.
     */
    public Wall(
            Vector2d dimensions, Vector2d position,
            boolean isHorizontal, double boundsOffset, Color baseColor) {
        super(position, Vector2d.NULL);

        this.dimensions = dimensions;
        this.isHorizontal = isHorizontal;
        this.baseColor = baseColor;

        if (isHorizontal)
            boundsLine = new Vector2d(position.x, position.y + boundsOffset);
        else
            boundsLine = new Vector2d(position.x + boundsOffset, position.y);
    }

    /**
     * Returns the size of the wall's representation.
     *
     * @return The wall's dimensions.
     */
    public Vector2d getDimensions() {
        return dimensions;
    }

    /**
     * Returns whether the wall is oriented horizontally or vertically.
     * @return True if horizontal, false if vertical.
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Returns the position of the bounds line.
     *
     * This is the line that needs to be tested against for collisions.
     * @return The bounds line's position.
     */
    public Vector2d getBoundsLine() {
        return boundsLine;
    }

    public void adjustCollisionIntensity(double delta) {
        collisionIntensity = Math.max(
                Math.min(collisionIntensity + delta, 1), 0);
    }

    /**
     * Calculates and returns the color of the wall.
     *
     * The color depends on the current collision intensity, which increases
     * when a ball hits and decreases over time.
     * @return The wall's current color.
     */
    public Color getColor() {
        return new Color(
                baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(),
                35 + (int) (collisionIntensity * 150));
    }
}