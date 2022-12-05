package dreamlink.utility.maths;

import org.joml.Vector4f;

public class Vector4fMaths {

    private static int offsetX = 24;
    private static int offsetY = 16;
    private static int offsetZ = 8;
    private static int offsetW = 0;

    public static Vector4f zero = new Vector4f(0f);
    public static Vector4f one = new Vector4f(1f);

    public static int toHex(Vector4f color) {
        var packedColor = 0;
        packedColor |= (int)(color.x * 255) << Vector4fMaths.offsetX;
        packedColor |= (int)(color.y * 255) << Vector4fMaths.offsetY;
        packedColor |= (int)(color.z * 255) << Vector4fMaths.offsetZ;
        packedColor |= (int)(color.w * 255) << Vector4fMaths.offsetW;
        return packedColor;
    }

    public static Vector4f fromHex(int hex) {
        return new Vector4f(
            ((hex >> Vector4fMaths.offsetX) & 0xFF) / 255f,
            ((hex >> Vector4fMaths.offsetY) & 0xFF) / 255f,
            ((hex >> Vector4fMaths.offsetZ) & 0xFF) / 255f,
            ((hex >> Vector4fMaths.offsetW) & 0xFF) / 255f
        );
    }

    public static Vector4f fromAlpha(float alpha) {
        return new Vector4f(1f, 1f, 1f, alpha);
    }

    public static Vector4f halfAlpha =  Vector4fMaths.fromAlpha(0.5f);
    public static Vector4f white = Vector4fMaths.fromHex(0xFFFFFFFF);
    public static Vector4f black = Vector4fMaths.fromHex(0x000000FF);
    public static Vector4f red = Vector4fMaths.fromHex(0xFF0000FF);
    public static Vector4f yellow = Vector4fMaths.fromHex(0xFFFF00FF);
    public static Vector4f orange = Vector4fMaths.fromHex(0xFFA500FF);
    public static Vector4f green = Vector4fMaths.fromHex(0x00FF00FF);
    public static Vector4f cyan = Vector4fMaths.fromHex(0x00FFFFFF);
    public static Vector4f blue = Vector4fMaths.fromHex(0x0000FFFF);
    public static Vector4f magenta = Vector4fMaths.fromHex(0xFF00FFFF);
    public static Vector4f overlayBackground = Vector4fMaths.fromHex(0x447284FF);
    public static Vector4f overlayHighlight = Vector4fMaths.fromHex(0x010081FF);
    public static Vector4f overlayWindow = Vector4fMaths.fromHex(0xEFBCE4FF);
    
}
