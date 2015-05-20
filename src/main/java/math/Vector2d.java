package math;

public class Vector2d {

    public static Vector2d NULL = new Vector2d();
    public double x;
    public double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d() {
        this(.0, .0);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public void setAngle(double newAngle) {
        double norm = norm();
        x = norm * Math.cos(newAngle);
        y = norm * Math.sin(newAngle);
    }

    public double norm() {
        return Math.sqrt(this.norm2());
    }

    public double norm2() {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    public void setNorm(double newNorm) {
        double angle = angle();
        x = newNorm * Math.cos(angle);
        y = newNorm * Math.sin(angle);
    }

    public Vector2d sum(Vector2d otherVector) {
        return new Vector2d(x + otherVector.x, y + otherVector.y);
    }

    public Vector2d sub(Vector2d otherVector) {
        return new Vector2d(x - otherVector.x, y - otherVector.y);
    }

    public Vector2d product(double k) {
        return new Vector2d(x * k, y * k);
    }

    public double scalar(Vector2d otherVector) {
        return x * otherVector.x + y * otherVector.y;
    }

    public boolean equals(Vector2d otherVector) {
        return x == otherVector.x && y == otherVector.y;
    }

    public Vector2d getNormed(double newNorm) {
        return product(Math.sqrt(newNorm / norm()));
    }

    public Vector2d getAngled(double newAngle) {
        return new Vector2d(Math.cos(newAngle), Math.sin(newAngle)).product(norm());
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}