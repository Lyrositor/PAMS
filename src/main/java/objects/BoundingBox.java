package objects;

class BoundingBox {

    public final BoundsType TYPE;

    private final double[] params;

    public BoundingBox(BoundsType type, double... params) {
        TYPE = type;
        this.params = params;
    }

    public double radius() {
        if (TYPE == BoundsType.CIRCLE)
            return params[0];
        return .0;
    }

    public double width() {
        if (TYPE == BoundsType.RECTANGLE)
            return params[0];
        return .0;
    }

    public double height() {
        if (TYPE == BoundsType.RECTANGLE)
            return params[1];
        return .0;
    }

    public double orientation() {
        if (TYPE == BoundsType.LINE)
            return params[0];
        return .0;
    }

}