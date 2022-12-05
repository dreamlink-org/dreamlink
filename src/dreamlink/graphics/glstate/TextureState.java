package dreamlink.graphics.glstate;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.texture.Texture;

public class TextureState implements AutoCloseable {

    // The 16th texture unit is a reserved "working area" where textures are created and edited.
    public static int maxTextureUnits = 15;

    private static Texture[] currentlyBoundTextures = new Texture[TextureState.maxTextureUnits];

    public static void bindTexture(int textureUnitID, Texture texture) {
        TextureState.currentlyBoundTextures[textureUnitID] = texture;
        GL42.glActiveTexture(GL42.GL_TEXTURE0 + textureUnitID);
        GL42.glBindTexture(GL42.GL_TEXTURE_2D, texture == null ? 0 : texture.textureID);
    }

    public static void initialize() {
        for(var ix = 0; ix < TextureState.maxTextureUnits; ix += 1) {
            TextureState.bindTexture(ix, null);
        }
    }

    private Texture previouslyBoundTexture;
    private int textureUnitID;

    public TextureState(int textureUnitID) {
        this.textureUnitID = textureUnitID;
        this.previouslyBoundTexture = TextureState.currentlyBoundTextures[textureUnitID];
    }

    public void setState(Texture texture) {
        if(TextureState.currentlyBoundTextures[this.textureUnitID] != texture) {
            TextureState.bindTexture(this.textureUnitID, texture);
        }
    }

    @Override
    public void close() {
        this.setState(this.previouslyBoundTexture);
    }
    
}
