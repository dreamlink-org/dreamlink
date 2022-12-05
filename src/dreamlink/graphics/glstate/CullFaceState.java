package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

public class CullFaceState implements AutoCloseable {

    private static boolean isCullFaceEnabled;

    public static void setCullFace(boolean enabled) {
        CullFaceState.isCullFaceEnabled = enabled;
        if(enabled) {
            GL42.glEnable(GL42.GL_CULL_FACE);
        } else {
            GL42.glDisable(GL42.GL_CULL_FACE);
        }
    }

    public static void initialize() {
        GL42.glDisable(0);
    }

    private boolean isPreviouslyEnabled;

    public CullFaceState() {
        this.isPreviouslyEnabled = CullFaceState.isCullFaceEnabled;
    }

    public void setState(boolean enabled) {
        if(CullFaceState.isCullFaceEnabled != enabled) {
            CullFaceState.setCullFace(enabled);
        }
    }

    @Override
    public void close() {
        this.setState(this.isPreviouslyEnabled);
    }
    
}
