package dreamlink.graphics.texture;

import dreamlink.graphics.texture.data.RGBA8TargetTextureData;
import dreamlink.window.Window;

public class PortalTexture extends Texture {

    public static PortalTexture instance = new PortalTexture();

    @Override
    public void setup() {
        super.setup();

        var resolution = Window.instance.getResolution();
        var textureData = new RGBA8TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
