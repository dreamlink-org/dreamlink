package dreamlink.graphics.framebuffer;

import org.lwjgl.opengl.GL42;

public class ColorAttachmentUnit {

    public static ColorAttachmentUnit color0 = new ColorAttachmentUnit(GL42.GL_COLOR_ATTACHMENT0);
    public static ColorAttachmentUnit color1 = new ColorAttachmentUnit(GL42.GL_COLOR_ATTACHMENT1);
    public static ColorAttachmentUnit color2 = new ColorAttachmentUnit(GL42.GL_COLOR_ATTACHMENT2);

    public int attachmentUnitID;

    public ColorAttachmentUnit(int attachmentID) {
        this.attachmentUnitID = attachmentID;
    }
    
}
