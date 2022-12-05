package dreamlink.graphics.texture;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.texture.data.ITextureData;

public class Texture {

    public int textureID;

    public void setup() {
        this.textureID = GL42.glGenTextures();

        GL42.glActiveTexture(GL42.GL_TEXTURE0 + TextureState.maxTextureUnits);
        GL42.glBindTexture(GL42.GL_TEXTURE_2D, this.textureID);
        GL42.glTexParameteri(GL42.GL_TEXTURE_2D, GL42.GL_TEXTURE_MIN_FILTER, GL42.GL_NEAREST);
        GL42.glTexParameteri(GL42.GL_TEXTURE_2D, GL42.GL_TEXTURE_MAG_FILTER, GL42.GL_NEAREST);
    }

    public void bufferData(ITextureData textureData) {
        GL42.glActiveTexture(GL42.GL_TEXTURE0 + TextureState.maxTextureUnits);
        GL42.glBindTexture(GL42.GL_TEXTURE_2D, this.textureID);
        textureData.bufferData();
    }

    public void destroy() {
        GL42.glDeleteTextures(this.textureID);
    }

    @Override
    public String toString() {
        return String.format("Texture(%s)", this.textureID);
    }

}
