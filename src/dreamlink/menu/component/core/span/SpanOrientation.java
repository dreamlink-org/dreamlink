package dreamlink.menu.component.core.span;

public class SpanOrientation {

    public int primaryAxis;
    public int secondaryAxis;

    public SpanOrientation(int axisID) {
        this.primaryAxis = axisID;
        this.secondaryAxis = 1 - axisID;
    }

    public static SpanOrientation horizontal = new SpanOrientation(0);
    public static SpanOrientation vertical = new SpanOrientation(1);
}