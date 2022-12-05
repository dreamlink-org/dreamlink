package dreamlink.graphics.framebuffer.terrain;

import dreamlink.graphics.framebuffer.ColorAttachment;
import dreamlink.graphics.framebuffer.DepthBuffer;
import dreamlink.graphics.framebuffer.FrameBuffer;
import dreamlink.graphics.texture.OpaqueTexture;

public class OpaqueTerrainFrameBuffer extends FrameBuffer {

    public static int attachmentID = 0;

    public static OpaqueTerrainFrameBuffer instance = new OpaqueTerrainFrameBuffer();

    public OpaqueTerrainFrameBuffer() {
        super(DepthBuffer.terrain);

        new ColorAttachment(
            this,
            OpaqueTerrainFrameBuffer.attachmentID,
            OpaqueTexture.instance
        );
    }
    
}
