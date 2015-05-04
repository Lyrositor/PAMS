package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    private static final double DENSITY = 7.0;
    private double rayon;

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
        BoundingBox bounds1 = bounds;
        BoundingBox bounds2 = bubble.getBounds();
        Vector2d pos1 = position;
        Vector2d pos2 = bubble.getPosition();
        Vector2d diff = pos2.sub(pos1);

        if (diff.norm() <= bounds1.radius() + bounds2.radius())
            return diff.setNorm(rayon);
        return null;
    }

    public Vector2d intersects(Wall wall) {
        BoundingBox bounds1 = bounds;
        BoundingBox bounds2 = wall.getBounds();
        Vector2d pos1 = position;
        Vector2d pos2 = wall.getPosition();
        Vector2d diff = pos2.sub(pos1);

        if (bounds2.orientation() == BoundingBox.HORIZONTAL)
            if (Math.abs(pos2.y - pos1.y) <= bounds2.width() + bounds1.radius())
                return new Vector2d(0, diff.y / Math.abs(diff.y) * bounds1.radius());
            else if (bounds2.orientation() == BoundingBox.VERTICAL)
                if (Math.abs(pos2.x - pos1.x) <= bounds2.width() + bounds1.radius())
                    return new Vector2d(diff.x / Math.abs(diff.x) * bounds1.radius(), 0);
        return null;
    }

}