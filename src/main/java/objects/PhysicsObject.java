package objects;

import math.Vector2d;

public class PhysicsObject {

    protected BoundingBox bounds;
    protected Vector2d position;
    protected Vector2d vitesse;
    protected Vector2d acceleration;

    public PhysicsObject(BoundingBox b, Vector2d p, Vector2d v) {
        bounds = b;
        position = p;
        vitesse = v;
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    public void setBounds(BoundingBox bounds) {
        this.bounds = bounds;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d p) {
        position = p;
    }

    public Vector2d getVitesse() {
        return vitesse;
    }

    public void setVitesse(Vector2d v) {
        vitesse = v;
    }
}

