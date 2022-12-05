package dreamlink.graphics.texture;

import dreamlink.Config;
import dreamlink.graphics.texture.data.RGBA16TargetTextureData;

public class TransparentAccumulatorTexture extends Texture {

    public static TransparentAccumulatorTexture instance = new TransparentAccumulatorTexture();

    public TransparentAccumulatorTexture() {
        super();
    }

    @Override
    public void setup() {
        super.setup();

        var resolution = Config.instance.resolution;
        var textureData = new RGBA16TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
