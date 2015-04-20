package objects;

public class BoundingBox {

    public final BoundsType TYPE;

    private final double[] params;

    public BoundingBox(BoundsType type, double[] params) {
        TYPE = type;
        this.params = params;
    }

    public double radius() {
        if (TYPE == BoundsType.CIRCLE)
            return params[0];
        return .0f;
    }

    public double width() {
        if (TYPE == BoundsType.RECTANGLE)
            return params[0];
        return .0f;
    }

    public double height() {
        if (TYPE == BoundsType.RECTANGLE)
            return params[1];
        return .0f;
    }

}