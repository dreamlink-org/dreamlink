package dreamlink.graphics.texture;

import java.io.BufferedInputStream;
import java.io.IOException;

import dreamlink.graphics.texture.data.RGBA8ImageTextureData;

public class OverlayTexture extends Texture {

    private static String texturePath = "texture/overlay.png";

    public static OverlayTexture instance = new OverlayTexture();

    @Override
    public void setup() {
        super.setup();
        try(
            var stream = this.getClass().getClassLoader().getResourceAsStream(OverlayTexture.texturePath);
            var bufferedStream = new BufferedInputStream(stream);
        ) {
            var textureData = RGBA8ImageTextureData.fromStream(bufferedStream);
            this.bufferData(textureData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
