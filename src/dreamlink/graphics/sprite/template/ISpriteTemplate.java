package dreamlink.graphics.sprite.template;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;

public interface ISpriteTemplate {

    public void writeToSpriteBatch(
        SpriteBatch spriteBatch,
        Vector2i position,
        Vector2i dimensions,
        SpriteHeight height
    );

}
