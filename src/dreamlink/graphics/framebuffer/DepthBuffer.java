package dreamlink.graphics.framebuffer;

import org.lwjgl.opengl.GL42;

import dreamlink.Config;
import dreamlink.logger.Logger;

public class DepthBuffer {

    public static DepthBuffer terrain = new DepthBuffer();
    public static DepthBuffer portal = new DepthBuffer();

    public int depthBufferID;

    public void setup() {
        Logger.instance.debug(String.format("Setting up depth buffer: %s", this));
        var resolution = Config.instance.resolution;
        this.depthBufferID = GL42.glGenRenderbuffers();
        GL42.glBindRenderbuffer(GL42.GL_RENDERBUFFER, this.depthBufferID);
        GL42.glRenderbufferStorage(
            GL42.GL_RENDERBUFFER, 
            GL42.GL_DEPTH_COMPONENT, 
            resolution.x, 
            resolution.y
        );
    }

    public void delete() {
        GL42.glDeleteRenderbuffers(this.depthBufferID);
    }

    @Override
    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("DepthBuffer(%s)", hash);
    }
    
}
