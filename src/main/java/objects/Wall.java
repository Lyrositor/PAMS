package objects;

import math.Vector2d;

import java.awt.*;

public class Wall extends PhysicsObject {

    private Color teinte;
    private double length;

    public Wall(double e, double l, double o, Vector2d p, Color t) {
        super(new BoundingBox(BoundsType.LINE, e, o), p, Vector2d.NULL);
        teinte = t;
        length = l;
    }

    public Vector2d getDimensions() {
        Vector2d dim;
        if (bounds.orientation() == BoundingBox.HORIZONTAL)
            dim = new Vector2d(length, bounds.width() * 2);
        else
            dim = new Vector2d(bounds.width() * 2, length);
        return dim;
    }
}

