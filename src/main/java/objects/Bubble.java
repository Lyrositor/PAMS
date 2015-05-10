package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    private static final double DENSITY = 7.0;

    public Bubble(double r, Vector2d p, Vector2d v) {
        super(new BoundingBox(BoundsType.CIRCLE, r), p, v);
    }

    public Color getColor() {
        return Color.red;
    }

    public double getRayon() {
        return bounds.radius();
    }

    public double getMass() {
        return Math.PI * Math.pow(getRayon(), 2) * DENSITY;
    }

    public Vector2d intersects(Bubble otherBubble) {
        BoundingBox otherBubbleBounds = otherBubble.getBounds();
        Vector2d otherBubblePosition = otherBubble.getPosition();
        Vector2d diff = otherBubblePosition.sub(position);

        if (diff.norm() <= bounds.radius() + otherBubbleBounds.radius())
            return diff.setNorm(getRayon());
        return null;
    }

    public Vector2d intersects(Wall wall) {
        BoundingBox wallBounds = wall.getBounds();
        Vector2d wallPos = wall.getPosition();
        Vector2d diff = wallPos.sub(position);

        if (wallBounds.orientation() == BoundingBox.HORIZONTAL) {
            if (Math.abs(wallPos.y - position.y) <= wallBounds.width() + bounds.radius())
                return new Vector2d(0, diff.y / Math.abs(diff.y) * bounds.radius());
        } else if (wallBounds.orientation() == BoundingBox.VERTICAL) {
            if (Math.abs(wallPos.x - position.x) <= wallBounds.width() + bounds.radius())
                return new Vector2d(diff.x / Math.abs(diff.x) * bounds.radius(), 0);
        }

        return null;
    }

}