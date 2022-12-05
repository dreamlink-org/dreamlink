package dreamlink.logger.simulation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import dreamlink.logger.ILogListener;
import dreamlink.logger.LogMessage;
import dreamlink.logger.Logger;

public class SynchronizedLogRelay implements ILogListener {

    public static SynchronizedLogRelay instance = new SynchronizedLogRelay();

    private Queue<LogMessage> messageBuffer;
    private List<ILogListener> logListeners;

    public SynchronizedLogRelay() {
        this.messageBuffer = new ArrayDeque<>();
        this.logListeners = new ArrayList<>();
    }

    public void addListener(ILogListener listener) {
        this.logListeners.add(listener);
    }

    public void setup() {
        Logger.instance.addListener(this);
    }

    @Override
    public void onLogMessage(LogMessage message) {
        synchronized(this) {
            this.messageBuffer.add(message);
        }
    }

    public void update() {
        synchronized(this) {
            while(!this.messageBuffer.isEmpty()) {
                var logMessage = this.messageBuffer.remove();
                for(var listener : this.logListeners) {
                    listener.onLogMessage(logMessage);
                }
            }
        }
    }
    
}
