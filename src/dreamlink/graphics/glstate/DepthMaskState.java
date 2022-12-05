package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

public class DepthMaskState implements AutoCloseable {

    private static boolean isDepthMaskEnabled;

    public static void setDepthMask(boolean isEnabled) {
        GL42.glDepthMask(isEnabled);
        DepthMaskState.isDepthMaskEnabled = isEnabled;
    }

    public static void initialize() {
        DepthMaskState.setDepthMask(true);
    }

    private boolean isPreviouslyEnabled;

    public DepthMaskState() {
        this.isPreviouslyEnabled = DepthMaskState.isDepthMaskEnabled;
    }

    public void setState(boolean enabled) {
        if(DepthMaskState.isDepthMaskEnabled != enabled) {
            DepthMaskState.setDepthMask(enabled);
        }
    }

    @Override
    public void close() {
        this.setState(this.isPreviouslyEnabled);
    }
    
}
