package dreamlink.graphics.texture;

import dreamlink.Config;
import dreamlink.graphics.texture.data.R8TargetTextureData;

public class TransparentRevealTexture extends Texture {

    public static TransparentRevealTexture instance = new TransparentRevealTexture();

    @Override
    public void setup() {
        super.setup();
        var resolution = Config.instance.resolution;
        var textureData = new R8TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
