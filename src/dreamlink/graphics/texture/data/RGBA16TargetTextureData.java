package dreamlink.graphics.texture.data;

import java.nio.ByteBuffer;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL42;

public class RGBA16TargetTextureData implements ITextureData {

    private Vector2i dimensions;

    public RGBA16TargetTextureData(Vector2i dimensions) {
        this.dimensions = new Vector2i(dimensions);
    }

    @Override
    public void bufferData() {
        GL42.glTexImage2D(
            GL42.GL_TEXTURE_2D, 
            0, 
            GL42.GL_RGBA16F, 
            this.dimensions.x,
            this.dimensions.y,
            0, 
            GL42.GL_RGBA, 
            GL42.GL_FLOAT, 
            (ByteBuffer)null
        );
    }
    
}
