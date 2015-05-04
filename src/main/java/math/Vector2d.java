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

    public Vector2d add(Vector2d vector2) {
        return new Vector2d(x + vector2.x, y + vector2.y);
    }

    public Vector2d sub(Vector2d vector2) {
        return new Vector2d(x - vector2.x, y - vector2.y);
    }

    public Vector2d multiply(double k) {
        return new Vector2d(x * k, y * k);
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

}