package dreamlink.window;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL42;

public class WindowGLCapabilitiesModule {

    public void setup() {
        GL.createCapabilities();
        GL42.glClearColor(0f, 0f, 0f, 1f);

        GL42.glPixelStorei(GL42.GL_UNPACK_ALIGNMENT, 1);

        // Enable openGL features
        GL42.glCullFace(GL42.GL_BACK);
        GL42.glEnable(GL42.GL_CLIP_DISTANCE0);
        GL42.glEnable(GL42.GL_BLEND);
        GL42.glEnable(GL42.GL_POLYGON_OFFSET_FILL);

        // Set up the color buffer for the default framebuffer.
        GL42.glDrawBuffers(new int[] {GL42.GL_COLOR_ATTACHMENT0});
    }
    
}
