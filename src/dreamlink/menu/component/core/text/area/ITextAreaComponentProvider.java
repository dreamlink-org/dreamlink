package dreamlink.menu.component.core.text.area;

import dreamlink.menu.component.core.text.TextCharacterState;

public interface ITextAreaComponentProvider {

    public void setCharacterState(
        int lineIndex, 
        int characterIndex, 
        TextCharacterState characterState
    );

    public int getLineCount();

    public int getCharacterCount(int lineIndex);
    
}
