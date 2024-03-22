package dreamlink.logger;

import org.joml.Vector4f;

import dreamlink.utility.maths.Vector4fMaths;

public class LogLevel {

    private static Vector4f logDebugColor = Vector4fMaths.fromHex(0x161616FF);
    private static Vector4f logInfoColor = Vector4fMaths.fromHex(0x29408AFF);
    private static Vector4f logWarnColor = Vector4fMaths.fromHex(0xA3600FFF);
    private static Vector4f logErrorColor = Vector4fMaths.fromHex(0x7A0B11FF);

    public static LogLevel debug = new LogLevel(0, "dbg", LogLevel.logDebugColor);
    public static LogLevel info = new LogLevel(1, "inf", LogLevel.logInfoColor);
    public static LogLevel warn = new LogLevel(2, "wrn", LogLevel.logWarnColor);
    public static LogLevel error = new LogLevel(3, "err", LogLevel.logErrorColor);

    public int level;
    public Vector4f color;
    public String tag;

    public LogLevel(int level, String tag, Vector4f color) {
        this.level = level;
        this.tag = tag;
        this.color = new Vector4f(color);
    }

    public String toString() {
        return String.format("Level(%s)", this.tag);
    }

}
