package dreamlink.menu.component.core.text.area;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import dreamlink.graphics.text.FontDecoration;
import dreamlink.menu.component.core.text.TextCharacterState;

public class WrappedTextAreaComponentProvider implements ITextAreaComponentProvider {

    private List<String> virtualLines;
    private FontDecoration fontDecoration;
    private Vector4f color;

    public WrappedTextAreaComponentProvider() {
        this.virtualLines = new ArrayList<>();
    }

    public void set(
        String text,
        FontDecoration fontDecoration,
        Vector4f color,
        int lineWidth
    ) {
        this.virtualLines.clear();
        this.fontDecoration = fontDecoration;
        this.color = new Vector4f(color);

        var builder = new StringBuilder();
        for(var ix = 0; ix < text.length(); ix += 1) {
            var character = text.charAt(ix);
            builder.append(character);
            if(builder.length() == lineWidth || character == '\n') {
                this.virtualLines.add(builder.toString());
                builder.setLength(0);
            }
        }
        
        if(builder.length() > 0) {
            this.virtualLines.add(builder.toString());
        }
    }

    @Override
    public void setCharacterState(int lineIndex, int characterIndex, TextCharacterState characterState) {
        var characterCount = this.virtualLines.get(lineIndex).length();
        if(characterIndex < characterCount) {
            characterState.set(
                this.virtualLines.get(lineIndex).charAt(characterIndex),
                this.fontDecoration,
                this.color
            );
        } else {
            characterState.clear();
        }
    }

    @Override
    public int getLineCount() {
        return this.virtualLines.size();
    }

    @Override
    public int getCharacterCount(int lineIndex) {
        return this.virtualLines.get(lineIndex).length();
    }
    
}
