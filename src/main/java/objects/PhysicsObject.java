package objects;

import math.Vector2d;

class PhysicsObject {

    protected BoundingBox bounds;
    protected Vector2d position;
    protected Vector2d vitesse;
    protected Vector2d acceleration;

    public PhysicsObject(BoundingBox b, Vector2d p, Vector2d v, Vector2d a) {
        bounds = b;
        position = p;
        vitesse = v;
        acceleration = a;
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

    public void applyForce(Vector2d position, Vector2d direction) {
    }

    public boolean intersects(PhysicsObject object) {
        BoundingBox bounds1 = this.bounds;
        BoundingBox bounds2 = object.getBounds();
        Vector2d pos1 = this.position;
        Vector2d pos2 = object.getPosition();
        BoundsType type1 = bounds1.TYPE;
        BoundsType type2 = bounds2.TYPE;

        if (type1 == BoundsType.CIRCLE && type1 == type2) {
        } else if (type1 == BoundsType.CIRCLE && type2 == BoundsType.RECTANGLE) {
        } else
            System.out.println("ERROR: Unsupported bounds intersection.");
        return false;
    }
}

