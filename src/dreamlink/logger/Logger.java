package dreamlink.logger;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    public static Logger instance = new Logger();

    private List<ILogListener> listeners;

    public Logger() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(ILogListener listener) {
        this.listeners.add(listener);
    }

    private void log(LogLevel level, String message) {
        var logMessage = new LogMessage(level, message);
        System.err.println(logMessage);

        for(var listener : this.listeners) {
            listener.onLogMessage(logMessage);
        }
    }

    public void debug(String message) {
        this.log(LogLevel.debug, message);
    }

    public void info(String message) {
        this.log(LogLevel.info, message);
    }

    public void warn(String message) {
        this.log(LogLevel.warn, message);
    }

    public void error(String message) {
        this.log(LogLevel.error, message);
    }
    
}
