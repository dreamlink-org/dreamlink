package dreamlink.logger;

import dreamlink.graphics.text.FontDecoration;
import dreamlink.menu.component.core.text.TextCharacterState;
import dreamlink.utility.maths.Vector4fMaths;

public class LogMessage {

    public String message;
    public LogLevel level;

    public LogMessage(LogLevel level, String message) {
        this.message = message;
        this.level = level;
    }

    public int getTotalCharacterCount() {
        return this.message.length() + this.level.tag.length() + 3;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.level.tag, this.message);
    }

    public void setCharacterState(int characterIndex, TextCharacterState state) {
        var fullMessage = this.toString();
        var character = fullMessage.charAt(characterIndex);
        var isRegularText = characterIndex == 0 || characterIndex > this.level.tag.length();
        state.set(
            character, 
            FontDecoration.normal,
            isRegularText ? Vector4fMaths.black : this.level.color
        );
    }
    
}
