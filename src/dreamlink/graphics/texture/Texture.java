package dreamlink.graphics.texture;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.texture.data.ITextureData;
import dreamlink.logger.Logger;

public class Texture {

    public int textureID;

    public void setup() {
        Logger.instance.debug(String.format("Setting up texture: %s", this));
        this.textureID = GL42.glGenTextures();
        GL42.glActiveTexture(GL42.GL_TEXTURE0 + TextureState.maxTextureUnits);
        GL42.glBindTexture(GL42.GL_TEXTURE_2D, this.textureID);
        GL42.glTexParameteri(GL42.GL_TEXTURE_2D, GL42.GL_TEXTURE_MIN_FILTER, GL42.GL_NEAREST);
        GL42.glTexParameteri(GL42.GL_TEXTURE_2D, GL42.GL_TEXTURE_MAG_FILTER, GL42.GL_NEAREST);
    }

    public void bufferData(ITextureData textureData) {
        Logger.instance.debug(String.format("Buffering data to texture: %s", this));
        GL42.glActiveTexture(GL42.GL_TEXTURE0 + TextureState.maxTextureUnits);
        GL42.glBindTexture(GL42.GL_TEXTURE_2D, this.textureID);
        textureData.bufferData();
    }

    public void destroy() {
        Logger.instance.debug(String.format("Destroying texture: %s", this));
        GL42.glDeleteTextures(this.textureID);
    }

    @Override
    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("Texture(%s)", hash);
    }

}
