package dreamlink.graphics.sprite.template;


import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.Vector4fMaths;

public class BorderSpriteTemplate implements ISpriteTemplate {

    public static BorderSpriteTemplate button = new BorderSpriteTemplate(
        OverlayTextureSample.buttonTop,
        OverlayTextureSample.buttonBottom,
        OverlayTextureSample.buttonLeft,
        OverlayTextureSample.buttonRight,
        OverlayTextureSample.buttonCenter,
        OverlayTextureSample.buttonTopLeft,
        OverlayTextureSample.buttonTopRight,
        OverlayTextureSample.buttonBottomLeft,
        OverlayTextureSample.buttonBottomRight
    );

    public static BorderSpriteTemplate buttonPressed = new BorderSpriteTemplate(
        OverlayTextureSample.buttonPressedTop,
        OverlayTextureSample.buttonPressedBottom,
        OverlayTextureSample.buttonPressedLeft,
        OverlayTextureSample.buttonPressedRight,
        OverlayTextureSample.buttonPressedCenter,
        OverlayTextureSample.buttonPressedTopLeft,
        OverlayTextureSample.buttonPressedTopRight,
        OverlayTextureSample.buttonPressedBottomLeft,
        OverlayTextureSample.buttonPressedBottomRight
    );

    public static BorderSpriteTemplate buttonDisabled = new BorderSpriteTemplate(
        OverlayTextureSample.buttonDisabledTop,
        OverlayTextureSample.buttonDisabledBottom,
        OverlayTextureSample.buttonDisabledLeft,
        OverlayTextureSample.buttonDisabledRight,
        OverlayTextureSample.buttonDisabledCenter,
        OverlayTextureSample.buttonDisabledTopLeft,
        OverlayTextureSample.buttonDisabledTopRight,
        OverlayTextureSample.buttonDisabledBottomLeft,
        OverlayTextureSample.buttonDisabledBottomRight
    );

    public static BorderSpriteTemplate dialog = new BorderSpriteTemplate(
        OverlayTextureSample.dialogTop,
        OverlayTextureSample.dialogBottom,
        OverlayTextureSample.dialogLeft,
        OverlayTextureSample.dialogRight,
        OverlayTextureSample.dialogCenter,
        OverlayTextureSample.dialogTopLeft,
        OverlayTextureSample.dialogTopRight,
        OverlayTextureSample.dialogBottomLeft,
        OverlayTextureSample.dialogBottomRight
    );

    public static BorderSpriteTemplate dialogDisabled = new BorderSpriteTemplate(
        OverlayTextureSample.dialogDisabledTop,
        OverlayTextureSample.dialogDisabledBottom,
        OverlayTextureSample.dialogDisabledLeft,
        OverlayTextureSample.dialogDisabledRight,
        OverlayTextureSample.dialogDisabledCenter,
        OverlayTextureSample.dialogDisabledTopLeft,
        OverlayTextureSample.dialogDisabledTopRight,
        OverlayTextureSample.dialogDisabledBottomLeft,
        OverlayTextureSample.dialogDisabledBottomRight
    );

    public static BorderSpriteTemplate window = new BorderSpriteTemplate(
        OverlayTextureSample.windowTop,
        OverlayTextureSample.windowBottom,
        OverlayTextureSample.windowLeft,
        OverlayTextureSample.windowRight,
        OverlayTextureSample.windowCenter,
        OverlayTextureSample.windowTopLeft,
        OverlayTextureSample.windowTopRight,
        OverlayTextureSample.windowBottomLeft,
        OverlayTextureSample.windowBottomRight
    );

    public static BorderSpriteTemplate dialogBlurred = new BorderSpriteTemplate(
        OverlayTextureSample.dialogBlurredTop,
        OverlayTextureSample.dialogBlurredBottom,
        OverlayTextureSample.dialogBlurredLeft,
        OverlayTextureSample.dialogBlurredRight,
        OverlayTextureSample.dialogBlurredCenter,
        OverlayTextureSample.dialogBlurredTopLeft,
        OverlayTextureSample.dialogBlurredTopRight,
        OverlayTextureSample.dialogBlurredBottomLeft,
        OverlayTextureSample.dialogBlurredBottomRight
    );

    public static BorderSpriteTemplate tab = new BorderSpriteTemplate(
        OverlayTextureSample.tabTop,
        OverlayTextureSample.tabBottom,
        OverlayTextureSample.tabLeft,
        OverlayTextureSample.tabRight,
        OverlayTextureSample.tabCenter,
        OverlayTextureSample.tabTopLeft,
        OverlayTextureSample.tabTopRight,
        OverlayTextureSample.tabBottomLeft,
        OverlayTextureSample.tabBottomRight
    );

    public static BorderSpriteTemplate tabBody = new BorderSpriteTemplate(
        OverlayTextureSample.buttonCenter,
        OverlayTextureSample.buttonBottom,
        OverlayTextureSample.buttonLeft,
        OverlayTextureSample.buttonRight,
        OverlayTextureSample.buttonCenter,
        OverlayTextureSample.buttonLeft,
        OverlayTextureSample.buttonRight,
        OverlayTextureSample.buttonBottomLeft,
        OverlayTextureSample.buttonBottomRight
    );

    public static BorderSpriteTemplate tabSelected = new BorderSpriteTemplate(
        OverlayTextureSample.tabTop,
        OverlayTextureSample.tabCenter,
        OverlayTextureSample.tabLeft,
        OverlayTextureSample.tabRight,
        OverlayTextureSample.tabCenter,
        OverlayTextureSample.tabTopLeft,
        OverlayTextureSample.tabTopRight,
        OverlayTextureSample.tabLeft,
        OverlayTextureSample.tabRight
    );

    public static BorderSpriteTemplate separator = new BorderSpriteTemplate(
        OverlayTextureSample.separatorTop,
        OverlayTextureSample.separatorBottom,
        OverlayTextureSample.separatorLeft,
        OverlayTextureSample.separatorRight,
        OverlayTextureSample.separatorCenter,
        OverlayTextureSample.separatorTopLeft,
        OverlayTextureSample.separatorTopRight,
        OverlayTextureSample.separatorBottomLeft,
        OverlayTextureSample.separatorBottomRight
    );

    private TextureSample topTextureSample;
    private TextureSample bottomTextureSample;
    private TextureSample leftTextureSample;
    private TextureSample rightTextureSample;
    private TextureSample centerTextureSample;
    private TextureSample topLeftTextureSample;
    private TextureSample topRightTextureSample;
    private TextureSample bottomLeftTextureSample;
    private TextureSample bottomRightTextureSample;

    public BorderSpriteTemplate(
        TextureSample topTextureSample,
        TextureSample bottomTextureSample,
        TextureSample leftTextureSample,
        TextureSample rightTextureSample,
        TextureSample centerTextureSample,
        TextureSample topLeftTextureSample,
        TextureSample topRightTextureSample,
        TextureSample bottomLeftTextureSample,
        TextureSample bottomRightTextureSample
    ) {
        this.topTextureSample = topTextureSample;
        this.bottomTextureSample = bottomTextureSample;
        this.leftTextureSample = leftTextureSample;
        this.rightTextureSample = rightTextureSample;
        this.centerTextureSample = centerTextureSample;
        this.topLeftTextureSample = topLeftTextureSample;
        this.topRightTextureSample = topRightTextureSample;
        this.bottomLeftTextureSample = bottomLeftTextureSample;
        this.bottomRightTextureSample = bottomRightTextureSample;
    }

    @Override
    public void writeToSpriteBatch(
        SpriteBatch spriteBatch,
        Vector2i position,
        Vector2i dimensions,
        SpriteHeight height
    ) {
        var texturePosition = new Vector2i();
        var textureDimensions = new Vector2i();

        spriteBatch.writeTextureSample(
            texturePosition.set(position),
            textureDimensions.set(dimensions),
            height,
            this.centerTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition.set(position),
            textureDimensions.set(dimensions.x, this.topTextureSample.dimensions.y),
            height,
            this.topTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition
                .set(position)
                .add(0, dimensions.y - this.bottomTextureSample.dimensions.y),
            textureDimensions
                .set(dimensions.x, this.bottomTextureSample.dimensions.y),
            height,
            this.bottomTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition.set(position),
            textureDimensions.set(this.leftTextureSample.dimensions.x, dimensions.y),
            height,
            this.leftTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition
                .set(position)
                .add(dimensions.x - this.rightTextureSample.dimensions.x, 0),
            textureDimensions
                .set(this.rightTextureSample.dimensions.x, dimensions.y),
            height,
            this.rightTextureSample, 
            Vector4fMaths.white
        );
        
        spriteBatch.writeTextureSample(
            texturePosition.set(position),
            textureDimensions.set(this.topLeftTextureSample.dimensions),
            height,
            this.topLeftTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition
                .set(position)
                .add(dimensions.x - this.topRightTextureSample.dimensions.x, 0),
            textureDimensions.set(this.topRightTextureSample.dimensions),
            height,
            this.topRightTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition
                .set(position)
                .add(0, dimensions.y - this.bottomLeftTextureSample.dimensions.y),
            textureDimensions.set(this.bottomLeftTextureSample.dimensions),
            height,
            this.bottomLeftTextureSample, 
            Vector4fMaths.white
        );

        spriteBatch.writeTextureSample(
            texturePosition
                .set(position)
                .add(dimensions)
                .sub(this.bottomRightTextureSample.dimensions),
            textureDimensions.set(this.bottomRightTextureSample.dimensions),
            height,
            this.bottomRightTextureSample, 
            Vector4fMaths.white
        );
    }
}
