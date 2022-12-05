package dreamlink.graphics.texture;

import dreamlink.Config;
import dreamlink.graphics.texture.data.RGBA8TargetTextureData;

public class OpaqueTexture extends Texture {

    public static OpaqueTexture instance = new OpaqueTexture();

    public OpaqueTexture() {
        super();
    }

    @Override
    public void setup() {
        super.setup();

        var resolution = Config.instance.resolution;
        var textureData = new RGBA8TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
