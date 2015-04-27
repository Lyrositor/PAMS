package objects;

import math.Vector2d;

import java.awt.*;

public class Wall extends PhysicsObject {

    private double largeur;
    private double hauteur;
    private Color teinte;

    public Wall(int l, int h, Vector2d p, Color t) {
        super(new BoundingBox(BoundsType.LINE, l, h), p, Vector2d.NULL);
        largeur = l;
        hauteur = h;
        teinte = t;
    }
}

