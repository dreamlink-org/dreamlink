package dreamlink.menu.component.core.span;

public class SpanAlignment {

    public static SpanAlignment start = new SpanAlignment(0.0f);
    public static SpanAlignment center = new SpanAlignment(0.5f);
    public static SpanAlignment end = new SpanAlignment(1.0f);

    public float factor;

    public SpanAlignment(float factor) {
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
