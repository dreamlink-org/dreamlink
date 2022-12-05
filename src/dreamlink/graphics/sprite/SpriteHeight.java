package dreamlink.graphics.sprite;

public class SpriteHeight {

    public static SpriteHeight background = new SpriteHeight(0);
    public static SpriteHeight menu = new SpriteHeight(1);
    public static SpriteHeight cursorPayload = new SpriteHeight(2);
    public static SpriteHeight cursor = new SpriteHeight(3);
    public static SpriteHeight hud = new SpriteHeight(4);

    public int height;

    public SpriteHeight(int height) {
        this.height = height;
    }
    
}
