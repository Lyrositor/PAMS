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
        this(0.0, 0.0);
    }

    public double norm() {
        return Math.sqrt(this.norm2());
    }

    public double norm2() {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

}