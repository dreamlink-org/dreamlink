package dreamlink.graphics.framebuffer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.glstate.FrameBufferState;

public class FrameBuffer {

    public int frameBufferID;
    private DepthBuffer depthBuffer;
    private List<ColorAttachment> colorAttachments;

    public FrameBuffer(DepthBuffer depthBuffer) {
        this.depthBuffer = depthBuffer;
        this.colorAttachments = new ArrayList<>();
    }

    public void addColorAttachment(ColorAttachment colorAttachment) {
        this.colorAttachments.add(colorAttachment);
    }
    public void setup() {
        this.frameBufferID = GL42.glGenFramebuffers();

        try(var frameBufferState = new FrameBufferState()) {
            frameBufferState.setState(this);

            GL42.glFramebufferRenderbuffer(
                GL42.GL_FRAMEBUFFER, 
                GL42.GL_DEPTH_ATTACHMENT, 
                GL42.GL_RENDERBUFFER, 
                this.depthBuffer.depthBufferID
            );

            var drawBuffers = new int[this.colorAttachments.size()];
            for(var ix = 0; ix < this.colorAttachments.size(); ix += 1) {
                var colorAttachment = this.colorAttachments.get(ix);
                colorAttachment.setup();
                drawBuffers[ix] = colorAttachment.getColorAttachmentGLID();
            }

            GL42.glDrawBuffers(drawBuffers);
        } 
    }

}
