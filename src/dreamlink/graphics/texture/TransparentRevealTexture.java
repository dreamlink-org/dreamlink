package dreamlink.graphics.texture;

import dreamlink.graphics.texture.data.R8TargetTextureData;
import dreamlink.window.Window;

public class TransparentRevealTexture extends Texture {

    public static TransparentRevealTexture instance = new TransparentRevealTexture();

    @Override
    public void setup() {
        super.setup();
        var resolution = Window.instance.getResolution();
        var textureData = new R8TargetTextureData(resolution);
        this.bufferData(textureData);
    }
    
}
