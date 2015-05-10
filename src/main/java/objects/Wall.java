package objects;

import math.Vector2d;

import java.awt.*;

public class Wall extends PhysicsObject {

    private Vector2d dimensions;
    private Vector2d boundsLine;
    private Color color;
    private boolean isHorizontal;  // true: horizontal, false: vertical

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

    public Vector2d getDimensions() {
        return dimensions;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public Vector2d getBoundsLine() {
        return boundsLine;
    }

    public Color getColor() {
        return color;
    }
}

