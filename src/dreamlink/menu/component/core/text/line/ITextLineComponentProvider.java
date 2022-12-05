package dreamlink.menu.component.core.text.line;

import dreamlink.menu.component.core.text.TextCharacterState;

public interface ITextLineComponentProvider {

    public void setCharacterState(int characterIndex, TextCharacterState characterState);

    public int getCharacterCount();
    
}
