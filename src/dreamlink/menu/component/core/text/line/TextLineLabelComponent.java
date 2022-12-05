package dreamlink.menu.component.core.text.line;

import org.joml.Vector4f;

import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.text.TextCharacterState;

public class TextLineLabelComponent extends WrapperComponent {

    private class InternalTextLineComponentProvider implements ITextLineComponentProvider {

        @Override
        public void setCharacterState(int characterIndex, TextCharacterState state) {
            state.set(
                TextLineLabelComponent.this.text.charAt(characterIndex),
                TextLineLabelComponent.this.fontDecoration,
                TextLineLabelComponent.this.color
            );
        }

        @Override
        public int getCharacterCount() {
            return TextLineLabelComponent.this.text.length();
        }

    }

    private BaseMenuComponent component;

    private String text;
    private FontDecoration fontDecoration;
    private Vector4f color;

    public TextLineLabelComponent(
        String text,
        FontDecoration fontDecoration,
        Vector4f color
    ) {
        this.text = text;
        this.fontDecoration = fontDecoration;
        this.color = new Vector4f(color);

        this.component = new BoxComponent(
            new TextLineComponent(new InternalTextLineComponentProvider()),
            BoxDimension.fixed(OverlayTextureSample.glyphDimensions.x * this.text.length()),
            BoxDimension.fixed(OverlayTextureSample.glyphDimensions.y)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}