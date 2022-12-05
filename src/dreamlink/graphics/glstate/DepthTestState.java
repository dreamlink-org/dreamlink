package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

public class DepthTestState implements AutoCloseable {

    private static boolean isDepthTestEnabled;

    public static void setDepthTest(boolean isEnabled) {
        DepthTestState.isDepthTestEnabled = isEnabled;
        if(isEnabled) {
            GL42.glEnable(GL42.GL_DEPTH_TEST);
        } else {
            GL42.glDisable(GL42.GL_DEPTH_TEST);
        }
    }

    public static void initialize() {
        DepthTestState.setDepthTest(false);
    }

    private boolean isPreviouslyEnabled;

    public DepthTestState() {
        this.isPreviouslyEnabled = DepthTestState.isDepthTestEnabled;
    }

    public void setState(boolean enabled) {
        if(DepthTestState.isDepthTestEnabled != enabled) {
            DepthTestState.setDepthTest(enabled);
        }
    }

    @Override
    public void close() {
        this.setState(this.isPreviouslyEnabled);
    }
    
}
