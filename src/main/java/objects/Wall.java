package objects;

import math.Vector2d;

import java.awt.*;

/**
 * Represents a static wall.
 * <p>
 * The wall is defined by two objects: a rectangle, which describes its
 * appearance, and an infinite line, which be horizontal or vertical, which
 * represents the boundary for the wall.
 */
public class Wall extends PhysicsObject {

    private final Vector2d dimensions;
    private final Vector2d boundsLine;
    private final Color color;
    private final boolean isHorizontal;

    /**
     * Constructs a new static wall.
     *
     * @param dimensions The width and height of the wall's representation.
     * @param position The position of the top-left corner of the wall.
     * @param isHorizontal Whether the wall is horizontal or vertical.
     * @param boundsOffset The distance along one coordinate between the wall's
     *                     position and its bounds.
     * @param color The wall's color.
     */
    public Wall(
            Vector2d dimensions, Vector2d position,
            boolean isHorizontal, double boundsOffset, Color color) {
        super(position, Vector2d.NULL);

        this.dimensions = dimensions;
        this.isHorizontal = isHorizontal;
        this.color = color;

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

    /**
     * Returns the color of the wall.
     *
     * @return The wall's color.
     */
    public Color getColor() {
        return color;
    }
}