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

    public Vector2d add(Vector2d otherVector) {
        return new Vector2d(x + otherVector.x, y + otherVector.y);
    }

    public Vector2d sub(Vector2d otherVector) {
        return new Vector2d(x - otherVector.x, y - otherVector.y);
    }

    public Vector2d multiply(double k) {
        return new Vector2d(x * k, y * k);
    }

    public boolean equals(Vector2d otherVector) {
        return x == otherVector.x && y == otherVector.y;
    }

    public double norm() {
        return Math.sqrt(this.norm2());
    }

    public double norm2() {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public Vector2d setNorm(double length) {
        double k = Math.sqrt(length / norm());
        return new Vector2d(k * x, k * y);
    }

    public double scalar(Vector2d otherVector) {
        return x * otherVector.x + y * otherVector.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}