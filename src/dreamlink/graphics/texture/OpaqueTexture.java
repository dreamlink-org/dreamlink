package dreamlink.graphics.texture;

import dreamlink.graphics.texture.data.RGBA8TargetTextureData;
import dreamlink.window.Window;

public class OpaqueTexture extends Texture {

    public static OpaqueTexture instance = new OpaqueTexture();

    @Override
    public void setup() {
        super.setup();

        var resolution = Window.instance.getResolution();
        var textureData = new RGBA8TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
