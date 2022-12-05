package dreamlink.menu.component.core.box;

public class BoxAlignment {

    public static BoxAlignment start = new BoxAlignment(0.0f);
    public static BoxAlignment center = new BoxAlignment(0.5f);
    public static BoxAlignment end = new BoxAlignment(1.0f);

    public float factor;

    public BoxAlignment(float factor) {
        this.factor = factor;
    }

    public int getOffset(
        int parentWidth, 
        int childWidth
    ) {
        var delta = parentWidth - childWidth;
        return (int)(delta * this.factor);
    }

}
