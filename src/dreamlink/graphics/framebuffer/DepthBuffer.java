package dreamlink.graphics.framebuffer;

import org.lwjgl.opengl.GL42;

import dreamlink.Config;

public class DepthBuffer {

    public static DepthBuffer terrain = new DepthBuffer();
    public static DepthBuffer portal = new DepthBuffer();

    public int depthBufferID;

    public void setup() {
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
    
}
