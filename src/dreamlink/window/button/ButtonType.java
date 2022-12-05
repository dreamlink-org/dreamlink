package dreamlink.window.button;

public class ButtonType {

    public static ButtonType mouse = new ButtonType(0);
    public static ButtonType keyboard = new ButtonType(64);

    public int offset;

    public ButtonType(int offset) {
        this.offset = offset;
    }

}
