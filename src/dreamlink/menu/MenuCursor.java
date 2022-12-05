package dreamlink.menu;

import dreamlink.window.Window;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.texture.sample.OverlayTextureSample;

public class MenuCursor {

    public static MenuCursor arrowCursor = new MenuCursor(
        OverlayTextureSample.cursorArrow, 
        OverlayTextureSample.cursorArrow.dimensions, 
        new Vector2i(3, 0)
    );

    public static MenuCursor pointerCursor = new MenuCursor(
        OverlayTextureSample.cursorPointer, 
        OverlayTextureSample.cursorPointer.dimensions, 
        new Vector2i(7, 0)
    );

    public static MenuCursor dragCursor = new MenuCursor(
        OverlayTextureSample.cursorGrab, 
        OverlayTextureSample.cursorGrab.dimensions,
        new Vector2i(8, 3)
    );

    private Vector2i dimensions;
    private Vector2i offset;
    private ISpriteTemplate sprite;

    public MenuCursor(ISpriteTemplate sprite, Vector2i dimensions, Vector2i offset) {
        this.sprite = sprite;
        this.dimensions = new Vector2i(dimensions);
        this.offset = new Vector2i(offset);
    }

    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var mousePosition = Window.instance.getMousePosition();
        var offsetMousePosition = new Vector2i(mousePosition).sub(this.offset);
        this.sprite.writeToSpriteBatch(
            spriteBatch,
            offsetMousePosition,
            this.dimensions, 
            SpriteHeight.cursor
        );
    }

}
