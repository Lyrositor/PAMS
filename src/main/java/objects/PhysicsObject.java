package objects;

import math.Vector2d;

public class PhysicsObject {

    protected Vector2d position;
    protected Vector2d speed;

    public PhysicsObject(Vector2d position, Vector2d speed) {
        this.position = position;
        this.speed = speed;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d p) {
        position = p;
    }

    public Vector2d getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2d v) {
        speed = v;
    }
}

