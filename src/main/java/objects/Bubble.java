package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    private double rayon;

    private static final double DENSITY = 7.0;

    public Bubble(double r, Vector2d p, Vector2d v) {
        super(new BoundingBox(BoundsType.CIRCLE, r), p, v);
        rayon = r;
    }
    //il faudrait, comme on avait dit, lier certains paramètres entre eux : la couleur et la vitesse, la hauteur du son et la taille du rayon...


    public Color getColor() {
        return new Color(0, 0, 0);
    }

    public double getRayon() {
        return rayon;
    }

    public double getMass() {
        return Math.PI * Math.pow(rayon, 2) * DENSITY;
    }

    public Vector2d intersects(Bubble bubble) {
        return null;
    }

    public Vector2d intersects(Wall wall) {
        return null;
    }
}