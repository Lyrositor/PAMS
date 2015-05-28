package org.pcc.pams.math;

/**
 * Represents a 2D vector.
 * <p>
 * Information is stored internally as cartesian coordinates, but they can be
 * converted to polar coordinates through functions.
 */
public class Vector2d {

    /**
     * The null vector (0, 0).
     */
    public static final Vector2d NULL = new Vector2d(0, 0);
    /**
     * The {@code x} coordinate.
     */
    public double x;
    /**
     * The {@code y} coordinate.
     */
    public double y;

    /**
     * Creates a new vector with the specified coordinates.
     *
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates and returns the polar coordinates' angle.
     *
     * @return The vector's angle.
     */
    public double angle() {
        return Math.atan2(y, x);
    }

    /**
     * Changes the vector's direction by modifying its angle.
     *
     * Internally converts polar coordinates to cartesian coordinates to
     * achieve this.
     * @param newAngle The new angle of the vector's direction.
     */
    public void setAngle(double newAngle) {
        double norm = norm();
        x = norm * Math.cos(newAngle);
        y = norm * Math.sin(newAngle);
    }

    /**
     * Calculates and returns the norm (or radius) of the vector.
     *
     * If comparing against another norm, it is computationally less expensive
     * to use {@link #norm2()}.
     * @return The vector's norm.
     */
    public double norm() {
        return Math.sqrt(this.norm2());
    }

    /**
     * Calculates and returns the square of the norm (or radius) of the vector.
     *
     * This is faster than calculating the norm itself.
     * @return The square of the vector's norm.
     */
    public double norm2() {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    /**
     * Sets the norm to a new value.
     *
     * Internally converts polar coordinates to cartesian coordinates to
     * achieve this.
     * @param newNorm The new value of the norm.
     */
    public void setNorm(double newNorm) {
        double angle = angle();
        x = newNorm * Math.cos(angle);
        y = newNorm * Math.sin(angle);
    }

    /**
     * Checks if two vectors have the same coordinates.
     *
     * @param otherVector The other vector to check against.
     * @return True if both of their coordinates are equal, false otherwise.
     */
    public boolean equals(Vector2d otherVector) {
        return x == otherVector.x && y == otherVector.y;
    }

    /**
     * Returns the scalar product of this vector and another one.
     *
     * @param otherVector The vector to multiply with.
     * @return The scalar product of the two vectors' coordinates.
     */
    public double scalar(Vector2d otherVector) {
        return x * otherVector.x + y * otherVector.y;
    }

    /**
     * Returns a vector that is the sum of this vector and otherVector.
     *
     * Does not change this vector's value.
     * @param otherVector The other vector to use for the sum.
     * @return A vector whose coordinates are the sum of this vector and
     * {@code otherVector}.
     */
    public Vector2d sum(Vector2d otherVector) {
        return new Vector2d(x + otherVector.x, y + otherVector.y);
    }

    /**
     * Returns a vector that is the difference of this vector and otherVector.
     *
     * Does not change this vector's value.
     * @param otherVector The other vector to use for the difference.
     * @return A vector whose coordinates are the difference of this vector and
     * {@code otherVector}.
     */
    public Vector2d sub(Vector2d otherVector) {
        return new Vector2d(x - otherVector.x, y - otherVector.y);
    }

    /**
     * Returns a vector that is the product of this vector and a scalar.
     *
     * Does not change this vector's value.
     * @param k The scalar to multiply by.
     * @return A vector whose coordinates are the product of this vector's
     * coordinates and {@code k}.
     */
    public Vector2d product(double k) {
        return new Vector2d(x * k, y * k);
    }

    /**
     * Returns a version of this vector with a different norm.
     *
     * Can be used to create a "truncated" or "stretched" vector. Does not
     * change this vector's value.
     * @param newNorm The new norm to apply.
     * @return A vector with the same direction but the new norm.
     */
    public Vector2d getNormed(double newNorm) {
        double angle = angle();
        return new Vector2d(
                newNorm * Math.cos(angle),
                newNorm * Math.sin(angle));
    }

    /**
     * Returns a version of this vector with a different angle.
     *
     * Can be used to create a "rotated" vector. Does not change this vector's
     * value.
     * @param newAngle The new angle to apply.
     * @return A vector with the same norm but the new direction.
     */
    public Vector2d getAngled(double newAngle) {
        return new Vector2d(
                Math.cos(newAngle), Math.sin(newAngle)).product(norm());
    }

    /**
     * Returns a simple representation of the vector, in cartesian coordinates.
     *
     * @return A string characterizing the vector.
     */
    public String toString() {
        return "Vector2d(" + x + ", " + y + ")";
    }
}