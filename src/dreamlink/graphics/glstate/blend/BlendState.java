package dreamlink.graphics.glstate.blend;

import org.lwjgl.opengl.GL42;

public class BlendState implements AutoCloseable {

    private static int maxDrawBuffers = 4;
    private static BlendMode[] blendModes = new BlendMode[BlendState.maxDrawBuffers];

    private static void setBlendMode(int index, BlendMode blendMode) {
        BlendState.blendModes[index] = blendMode;
        GL42.glBlendFunci(index, blendMode.srcFactor, blendMode.dstFactor);
    }

    public static void initialize() {
        for(var ix = 0; ix < BlendState.maxDrawBuffers; ix++) {
            BlendState.setBlendMode(ix, BlendMode.noBlend);
        }
    } 

    private BlendMode previousBlendMode;
    private int index;

    public BlendState(int index) {
        this.index = index;
        this.previousBlendMode = BlendState.blendModes[index];
    }

    public BlendState() {
        this(0);
    }

    public void setState(BlendMode blendMode) {
        if(BlendState.blendModes[this.index] != blendMode) {
            BlendState.setBlendMode(this.index, blendMode);
        }
    }

    @Override
    public void close() {
        this.setState(this.previousBlendMode);
    }
    
}
