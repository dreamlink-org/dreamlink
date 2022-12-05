package dreamlink.graphics.framebuffer.terrain;

import dreamlink.graphics.framebuffer.ColorAttachment;
import dreamlink.graphics.framebuffer.DepthBuffer;
import dreamlink.graphics.framebuffer.FrameBuffer;
import dreamlink.graphics.texture.PortalTexture;

public class PortalFrameBuffer extends FrameBuffer {

    public static int attachmentID = 0;

    public static PortalFrameBuffer instance = new PortalFrameBuffer();

    public PortalFrameBuffer() {
        super(DepthBuffer.portal);

        new ColorAttachment(
            this,
            PortalFrameBuffer.attachmentID,
            PortalTexture.instance
        );
    }
    
}
