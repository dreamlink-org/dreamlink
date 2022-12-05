package dreamlink.graphics.glstate.blend;

import org.lwjgl.opengl.GL42;

public class BlendMode {

    private static String unnamed = "unnamed";

    public static final BlendMode noBlend = new BlendMode(
        "noblend",
        GL42.GL_ONE, 
        GL42.GL_ZERO
    );

    public static final BlendMode alphaBlend = new BlendMode(
        "alphablend",
        GL42.GL_SRC_ALPHA, 
        GL42.GL_ONE_MINUS_SRC_ALPHA
    );

    public static final BlendMode accumulate = new BlendMode(
        "accumulate",
        GL42.GL_ONE, 
        GL42.GL_ONE
    );

    public String name;
    public int srcFactor;
    public int dstFactor;

    public BlendMode(String name, int srcFactor, int dstFactor) {
        this.name = name;
        this.srcFactor = srcFactor;
        this.dstFactor = dstFactor;
    }

    public BlendMode(int srcFactor, int dstFactor) {
        this(BlendMode.unnamed, srcFactor, dstFactor);
    }

    @Override
    public String toString() {
        return String.format("BlendMode(%s)", this.name);
    }
    
}
