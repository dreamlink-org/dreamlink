package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

import dreamlink.Config;
import dreamlink.graphics.framebuffer.FrameBuffer;
import dreamlink.window.Window;

public class FrameBufferState implements AutoCloseable {

    public static FrameBuffer currentlyBoundFrameBuffer;

    private static void setFrameBuffer(FrameBuffer frameBuffer) {
        FrameBufferState.currentlyBoundFrameBuffer = frameBuffer;
        if(frameBuffer != null) {
            var resolution = Config.instance.resolution;
            GL42.glBindFramebuffer(GL42.GL_FRAMEBUFFER, frameBuffer.frameBufferID);
            GL42.glViewport(0, 0, resolution.x, resolution.y);
        } else {
            var windowDimensions = Window.instance.getWindowDimensions();
            GL42.glBindFramebuffer(GL42.GL_FRAMEBUFFER, 0);
            GL42.glViewport(0, 0, windowDimensions.x, windowDimensions.y);
        }
    }

    public static void initialize() {
        FrameBufferState.setFrameBuffer(null);
    }

    private FrameBuffer previouslyBoundFrameBuffer;

    public FrameBufferState() {
        this.previouslyBoundFrameBuffer = FrameBufferState.currentlyBoundFrameBuffer;
    }

    public void setState(FrameBuffer frameBuffer) {
        if(FrameBufferState.currentlyBoundFrameBuffer != frameBuffer) {
            FrameBufferState.setFrameBuffer(frameBuffer);
        }
    }

    @Override
    public void close() {
        this.setState(this.previouslyBoundFrameBuffer);
    }
     

}
