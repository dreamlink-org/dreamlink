package dreamlink.menu.component.core.text;

import org.joml.Vector4f;

import dreamlink.graphics.text.CharacterTextureSampleLookup;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.TextureSample;

public class TextCharacterState {

    public char character;
    public FontDecoration fontDecoration;
    public Vector4f color;
    public boolean isUsed;

    public TextCharacterState() {
        this.color = new Vector4f();
    }

    public TextCharacterState set(char character, FontDecoration fontDecoration, Vector4f color) {
        this.character = character;
        this.fontDecoration = fontDecoration;
        this.color.set(color);
        this.isUsed = true;
        return this;
    }

    public TextureSample getGlyph() {
        if(!this.isUsed) {
            return null;
        }

        return CharacterTextureSampleLookup.instance.getTextureSample(
            this.character, 
            this.fontDecoration
        );
    }

    public TextCharacterState clear() {
        this.isUsed = false;
        return this;
    }
    
}
