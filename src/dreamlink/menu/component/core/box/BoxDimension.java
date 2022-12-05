package dreamlink.menu.component.core.box;

public class BoxDimension {

    public static BoxDimension fixed(int value, BoxAlignment alignment) {
        return new BoxDimension(value, value, alignment);
    }

    public static BoxDimension fixed(int value) {
        return BoxDimension.fixed(value, BoxAlignment.center);
    }

    public static BoxDimension max(int value, BoxAlignment alignment) {
        return new BoxDimension(0, value, alignment);
    }

    public static BoxDimension max(int value) {
        return BoxDimension.max(value, BoxAlignment.center);
    }

    public static BoxDimension min(int value, BoxAlignment alignment) {
        return new BoxDimension(value, Integer.MAX_VALUE, alignment);
    }

    public static BoxDimension min(int value) {
        return BoxDimension.min(value, BoxAlignment.center);
    }

    public static BoxDimension grow(BoxAlignment alignment) {
        return BoxDimension.min(0, alignment);
    }

    public static BoxDimension grow() {
        return BoxDimension.grow(BoxAlignment.center);
    }

    public static BoxDimension wrap() {
        return BoxDimension.max(0, BoxAlignment.start);
    }

    public int minValue;
    public int maxValue;
    public BoxAlignment alignment;

    public BoxDimension(
        int minValue, 
        int maxValue, 
        BoxAlignment alignment
    ) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.alignment = alignment;
    }

    
}
