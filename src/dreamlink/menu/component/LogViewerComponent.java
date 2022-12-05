package dreamlink.menu.component;

import java.util.ArrayList;
import java.util.List;

import dreamlink.logger.LogMessage;
import dreamlink.logger.simulation.SynchronizedLogRelay;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.text.TextCharacterState;
import dreamlink.menu.component.core.text.area.ITextAreaComponentProvider;
import dreamlink.menu.component.core.text.area.TextAreaComponent;

public class LogViewerComponent extends WrapperComponent {

    private class InternalTextAreaComponentProvider implements ITextAreaComponentProvider {

        @Override
        public void setCharacterState(
            int lineIndex, 
            int characterIndex, 
            TextCharacterState characterState
        ) {
            var adjustedIndex = LogViewerComponent.this.logCursorStart + lineIndex;
            adjustedIndex %= LogViewerComponent.this.maxLogLines;
            var message = LogViewerComponent.this.logMessages.get(adjustedIndex);
            message.setCharacterState(characterIndex, characterState);
        }

        @Override
        public int getLineCount() {
            return LogViewerComponent.this.logMessages.size();
        }

        @Override
        public int getCharacterCount(int lineIndex) {
            var adjustedIndex = LogViewerComponent.this.logCursorStart + lineIndex;
            adjustedIndex %= LogViewerComponent.this.maxLogLines;
            var message = LogViewerComponent.this.logMessages.get(adjustedIndex);
            return message.getTotalCharacterCount();
        }
        
    }

    private int maxLogLines;
    private int logCursorStart;
    private List<LogMessage> logMessages;
    private TextAreaComponent component;

    public LogViewerComponent(int maxLogLines) {
        this.maxLogLines = maxLogLines;
        this.logMessages = new ArrayList<>();
        this.component = new TextAreaComponent(new InternalTextAreaComponentProvider());
    }

    public void setup() {
        SynchronizedLogRelay.instance.addListener(this::onLogMessage);
    }

    private void onLogMessage(LogMessage logMessage) {
        if(this.logMessages.size() < this.maxLogLines) {
            this.logMessages.add(logMessage);
        } else {
            this.logMessages.set(this.logCursorStart, logMessage);
            this.logCursorStart += 1;
            this.logCursorStart %= this.maxLogLines;
        }
        this.component.scrollToBottom();
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
