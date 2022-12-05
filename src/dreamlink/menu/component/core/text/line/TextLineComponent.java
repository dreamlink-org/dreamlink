package dreamlink.menu.component.core.text.line;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.EmptyComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.text.TextCharacterState;

public class TextLineComponent extends WrapperComponent {

    private BaseMenuComponent component;
    private ITextLineComponentProvider provider;
    public int visibleCharacterCount;

    public TextLineComponent(ITextLineComponentProvider provider) {
        this.provider = provider;
        this.component = new BoxComponent(
            new EmptyComponent(),
            BoxDimension.min(OverlayTextureSample.glyphDimensions.x),
            BoxDimension.fixed(OverlayTextureSample.glyphDimensions.y)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        super.computeDimensions(availableSpace);
        this.visibleCharacterCount = this.component.getDimensions().x / OverlayTextureSample.glyphDimensions.x;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        super.writeToSpriteBatch(spriteBatch);
        var characterState = new TextCharacterState();
        var characterPosition = new Vector2i(this.component.getPosition());
        for(var ix = 0; ix < this.visibleCharacterCount; ix += 1) {
            if(ix >= this.provider.getCharacterCount()) {
                break;
            }

            this.provider.setCharacterState(ix, characterState);
            spriteBatch.writeTextureSample(
                characterPosition, 
                OverlayTextureSample.glyphDimensions, 
                SpriteHeight.menu,
                characterState.getGlyph(),
                characterState.color
            );

            characterPosition.x += OverlayTextureSample.glyphDimensions.x;
        }
    }
}
