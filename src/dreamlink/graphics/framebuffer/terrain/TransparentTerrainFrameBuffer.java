package dreamlink.graphics.framebuffer.terrain;

import dreamlink.graphics.framebuffer.ColorAttachment;
import dreamlink.graphics.framebuffer.DepthBuffer;
import dreamlink.graphics.framebuffer.FrameBuffer;
import dreamlink.graphics.texture.TransparentAccumulatorTexture;
import dreamlink.graphics.texture.TransparentRevealTexture;

public class TransparentTerrainFrameBuffer extends FrameBuffer {

    public static int accumulatorAttachmentID = 0;
    public static int revealAttachmentID = 1;

    public static TransparentTerrainFrameBuffer instance = new TransparentTerrainFrameBuffer();

    public TransparentTerrainFrameBuffer() {
        super(DepthBuffer.terrain);

        new ColorAttachment(
            this,
            TransparentTerrainFrameBuffer.accumulatorAttachmentID,
            TransparentAccumulatorTexture.instance
        );

        new ColorAttachment(
            this,
            TransparentTerrainFrameBuffer.revealAttachmentID,
            TransparentRevealTexture.instance
        );
    }
    
}
