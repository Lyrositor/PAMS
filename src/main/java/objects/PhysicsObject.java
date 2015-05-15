package objects;

import math.Vector2d;

class PhysicsObject {

    protected Vector2d position;
    protected Vector2d speed;

    public PhysicsObject(Vector2d position, Vector2d speed) {
        this.position = new Vector2d(position.x, position.y);
        this.speed = new Vector2d(speed.x, speed.y);
    }

    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    public void setPosition(Vector2d p) {
        position = new Vector2d(p.x, p.y);
    }

    public Vector2d getSpeed() {
        return new Vector2d(speed.x, speed.y);
    }

    public void setSpeed(Vector2d s) {
        speed = new Vector2d(s.x, s.y);
    }
}

