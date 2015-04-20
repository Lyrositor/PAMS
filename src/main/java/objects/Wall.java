package objects;

import math.Vector2d;

import java.awt.*;

public class Wall extends PhysicsObject {

    private double largeur;
    private double hauteur;
    private double coefficientElasticite;
    private Color teinte;

    public Wall(int l, int h, Vector2d p, int cE, Color t) {
        super(new BoundingBox(BoundsType.RECTANGLE, new double[]{l, h}));
        largeur = l;
        hauteur = h;
        position = p;
        coefficientElasticite = cE;
        teinte = t;
    }
}

