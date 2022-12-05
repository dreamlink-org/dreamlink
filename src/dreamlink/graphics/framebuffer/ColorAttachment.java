package dreamlink.graphics.framebuffer;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.texture.Texture;

public class ColorAttachment {

    public int colorAttachmentID;
    public Texture texture;
    public FrameBuffer renderTarget;

    public ColorAttachment(
        FrameBuffer renderTarget, 
        int drawBufferIndex,
        Texture texture
    ) {
        this.colorAttachmentID = drawBufferIndex;
        this.texture = texture;
        renderTarget.addColorAttachment(this);
    }

    public int getColorAttachmentGLID() {
        return GL42.GL_COLOR_ATTACHMENT0 + this.colorAttachmentID;
    }

    public void setup() {
        GL42.glFramebufferTexture2D(
            GL42.GL_FRAMEBUFFER, 
            this.getColorAttachmentGLID(),
            GL42.GL_TEXTURE_2D,
            this.texture.textureID,
            0
        );
    }
    
}
