package objects;

import math.Vector2d;

import java.awt.*;

public class Wall extends PhysicsObject {

    private Color teinte;

    public Wall(double e, double o, Vector2d p, Color t) {
        super(new BoundingBox(BoundsType.LINE, e, o), p, Vector2d.NULL);
        teinte = t;
    }
}

