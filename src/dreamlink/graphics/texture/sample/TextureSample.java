package dreamlink.graphics.texture.sample;

import org.joml.Vector2f;
import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.utility.maths.Vector4fMaths;

public class TextureSample implements ISpriteTemplate {

    private static Vector2i[] windingMasks = new Vector2i[] {
        new Vector2i(0, 0),
        new Vector2i(0, 1),
        new Vector2i(1, 1),
        new Vector2i(1, 0)
    };

    private Vector2i atlasDimensions;
    public Vector2i position;
    public Vector2i dimensions;
    public Vector2i animationStride;
    public int animationTotalFrames;
    public int animationRowFrames;
    public int animationStartFrame;
    public int animationSpeed;

    protected TextureSample(
        Vector2i atlasDimensions,
        Vector2i position, 
        Vector2i dimensions,
        Vector2i animationStride,
        int animationTotalFrames,
        int animationRowFrames,
        int animationStartFrame,
        int animationSpeed
    ) {
        this.atlasDimensions = new Vector2i(atlasDimensions);
        this.position = new Vector2i(position);
        this.dimensions = new Vector2i(dimensions);

        this.animationStride = new Vector2i(animationStride);
        this.animationTotalFrames = animationTotalFrames;
        this.animationStartFrame = animationStartFrame;
        this.animationRowFrames = animationRowFrames;
        this.animationSpeed = animationSpeed;
    }

    protected TextureSample(
        Vector2i atlasDimensions,
        Vector2i position, 
        Vector2i dimensions
    ) {
        this(
            atlasDimensions,
            position,
            dimensions,
            new Vector2i(0),
            1, 1, 0, 0
        );
    }

    public void getAnimationStride(Vector2f animationStride) {
        var animStrideX = (float)this.animationStride.x / this.atlasDimensions.x;
        var animStrideY = (float)this.animationStride.y / this.atlasDimensions.y;
        animationStride.set(animStrideX, animStrideY);
    }

    public void getTextureOffset(int windingIndex, Vector2f textureOffset) {
        var windingMask = TextureSample.windingMasks[windingIndex];
        var offsetX = (float)windingMask.x * this.dimensions.x + this.position.x;
        var offsetY = (float)windingMask.y * this.dimensions.y + this.position.y;
        textureOffset.set(
            offsetX / this.atlasDimensions.x,
            offsetY / this.atlasDimensions.y
        );
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
            this,
            Vector4fMaths.white
        );
    }

}

