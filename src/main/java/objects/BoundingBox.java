package objects;

public class BoundingBox {

    // Codes pour l'orientation du type LINE.
    public static final double VERTICAL = 0;
    public static final double HORIZONTAL = 1;
    public final BoundsType TYPE;
    private final double[] params;

    public BoundingBox(BoundsType type, double... params) {
        TYPE = type;
        this.params = params;
    }

    public double radius() {
        if (TYPE == BoundsType.CIRCLE)
            return params[0];
        return -1;
    }

    public double width() {
        if (TYPE == BoundsType.RECTANGLE || TYPE == BoundsType.LINE)
            return params[0];
        return -1;
    }

    public double height() {
        if (TYPE == BoundsType.RECTANGLE)
            return params[1];
        return -1;
    }

    public double orientation() {
        if (TYPE == BoundsType.LINE)
            return params[1];
        return -1;
    }

}