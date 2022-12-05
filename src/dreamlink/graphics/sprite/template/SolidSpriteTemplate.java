package dreamlink.graphics.sprite.template;

import org.joml.Vector2i;
import org.joml.Vector4f;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.utility.maths.Vector4fMaths;

public class SolidSpriteTemplate implements ISpriteTemplate {

    public static SolidSpriteTemplate white = new SolidSpriteTemplate(Vector4fMaths.white);
    public static SolidSpriteTemplate magenta = new SolidSpriteTemplate(Vector4fMaths.magenta);
    public static SolidSpriteTemplate black = new SolidSpriteTemplate(Vector4fMaths.black);
    public static SolidSpriteTemplate overlayHighlight = new SolidSpriteTemplate(Vector4fMaths.overlayHighlight);
    public static SolidSpriteTemplate overlayBackground = new SolidSpriteTemplate(Vector4fMaths.overlayBackground);
    public static SolidSpriteTemplate overlayWindow = new SolidSpriteTemplate(Vector4fMaths.overlayWindow);

    private Vector4f color;

    public SolidSpriteTemplate(Vector4f color) {
        this.color = new Vector4f(color);
    }

    @Override
    public void writeToSpriteBatch(
        SpriteBatch spriteBatch,
        Vector2i position, 
        Vector2i dimensions,
        SpriteHeight height
    ) {
        spriteBatch.writeTextureSample(
            position,
            dimensions,
            height,
            OverlayTextureSample.white,
            this.color
        );
    }
    
}
