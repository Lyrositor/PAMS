package objects;

import math.Vector2d;

/**
 * Represents a 2D object that has a position and a speed.
 */
class PhysicsObject {

    Vector2d position;
    Vector2d speed;

    /**
     * Constructs a new object with physical properties.
     *
     * @param position The object's initial position relative to the origin.
     * @param speed    The object's initial speed.
     */
    PhysicsObject(Vector2d position, Vector2d speed) {
        this.position = new Vector2d(position.x, position.y);
        this.speed = new Vector2d(speed.x, speed.y);
    }

    /**
     * Returns the object's current position.
     *
     * @return The position of the object.
     */
    public Vector2d getPosition() {
        return new Vector2d(position.x, position.y);
    }

    /**
     * Sets the object's current position to a new value.
     *
     * @param newPosition The new position.
     */
    public void setPosition(Vector2d newPosition) {
        position = new Vector2d(newPosition.x, newPosition.y);
    }

    /**
     * Returns the object's current speed.
     *
     * @return The speed of the object.
     */
    public Vector2d getSpeed() {
        return new Vector2d(speed.x, speed.y);
    }

    /**
     * Sets the object's current speed to a new value.
     *
     * @param newSpeed The new speed of the object.
     */
    public void setSpeed(Vector2d newSpeed) {
        speed = new Vector2d(newSpeed.x, newSpeed.y);
    }
}