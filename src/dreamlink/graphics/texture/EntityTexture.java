package dreamlink.graphics.texture;

import java.io.BufferedInputStream;
import java.io.IOException;

import dreamlink.graphics.texture.data.RGBA8ImageTextureData;

public class EntityTexture extends Texture {

    private static String texturePath = "texture/entity.png";

    public static EntityTexture instance = new EntityTexture();

    @Override
    public void setup() {
        super.setup();
        try(
            var stream = this.getClass().getClassLoader().getResourceAsStream(EntityTexture.texturePath);
            var bufferedStream = new BufferedInputStream(stream);
        ) {
            var textureData = RGBA8ImageTextureData.fromStream(bufferedStream);
            this.bufferData(textureData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
