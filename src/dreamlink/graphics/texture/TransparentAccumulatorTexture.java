package dreamlink.graphics.texture;

import dreamlink.graphics.texture.data.RGBA16TargetTextureData;
import dreamlink.window.Window;

public class TransparentAccumulatorTexture extends Texture {

    public static TransparentAccumulatorTexture instance = new TransparentAccumulatorTexture();

    @Override
    public void setup() {
        super.setup();

        var resolution = Window.instance.getResolution();
        var textureData = new RGBA16TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}