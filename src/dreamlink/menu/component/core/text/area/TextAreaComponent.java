package dreamlink.menu.component.core.text.area;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.EmptyComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.scroll.IScrollBarComponentProvider;
import dreamlink.menu.component.core.scroll.ScrollBarComponent;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.TextCharacterState;

public class TextAreaComponent extends WrapperComponent {

    private class InternalScrollBarComponentProvider implements IScrollBarComponentProvider {

        @Override
        public int getTotalItemCount() {
            return TextAreaComponent.this.provider.getLineCount();
        }

        @Override
        public int getVisibleItemCount() {
            return TextAreaComponent.this.characterDimensions.y;
        }

    }

    private BaseMenuComponent component;

    private ITextAreaComponentProvider provider;
    private BoxComponent boxComponent;
    private ScrollBarComponent scrollBarComponent;
    private Vector2i characterDimensions;

    public TextAreaComponent(ITextAreaComponentProvider provider) {
        this.characterDimensions = new Vector2i();
        this.provider = provider;
        this.scrollBarComponent = new ScrollBarComponent(new InternalScrollBarComponentProvider());
        this.boxComponent = new BoxComponent(
            new EmptyComponent(),
            BoxDimension.min(OverlayTextureSample.glyphDimensions.x),
            BoxDimension.min(OverlayTextureSample.glyphDimensions.y)
        );
        this.component = new SpanComponent(SpanOrientation.horizontal, SpanAlignment.start, 0)
            .addComponent(this.boxComponent)
            .addComponent(this.scrollBarComponent);
    }

    public void scrollToBottom() {
        this.scrollBarComponent.scrollToBottom();
    }

    public int getVisibleCharacterCount() {
        return this.characterDimensions.x;
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        super.computeDimensions(availableSpace);

        var boxDimensions = this.boxComponent.getDimensions();
        var scrollDimensions = this.scrollBarComponent.getDimensions();

        this.characterDimensions.set(
            boxDimensions.x / OverlayTextureSample.glyphDimensions.x,
            boxDimensions.y / OverlayTextureSample.glyphDimensions.y
        );

        super.computeDimensions(
            new Vector2i(
                this.characterDimensions.x * OverlayTextureSample.glyphDimensions.x + scrollDimensions.x,
                this.characterDimensions.y * OverlayTextureSample.glyphDimensions.y
            )
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        super.writeToSpriteBatch(spriteBatch);
        var characterState = new TextCharacterState();
        var characterPosition = new Vector2i(this.boxComponent.getPosition());
        var totalLines = this.provider.getLineCount();
        for(var y = 0; y < this.characterDimensions.y; y += 1) {
            var scrollAdjusted = y + this.scrollBarComponent.getScrollIndex();
            if(scrollAdjusted >= totalLines) {
                break;
            }

            var totalCharacters = this.provider.getCharacterCount(scrollAdjusted);
            for(var x = 0; x < this.characterDimensions.x; x += 1) {
                if(x >= totalCharacters) {
                    break;
                }

                this.provider.setCharacterState(scrollAdjusted, x, characterState);
                spriteBatch.writeTextureSample(
                    characterPosition, 
                    OverlayTextureSample.glyphDimensions, 
                    SpriteHeight.menu,
                    characterState.getGlyph(),
                    characterState.color
                );

                characterPosition.x += OverlayTextureSample.glyphDimensions.x;
            }

            characterPosition.x = this.boxComponent.getPosition().x;
            characterPosition.y += OverlayTextureSample.glyphDimensions.y;
        }
    }
    
}
