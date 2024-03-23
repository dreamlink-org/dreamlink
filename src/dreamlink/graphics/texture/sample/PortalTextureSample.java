package dreamlink.graphics.texture.sample;

import dreamlink.utility.maths.Vector2iMaths;
import dreamlink.window.Window;

public class PortalTextureSample extends TextureSample {

    public static PortalTextureSample instance = new PortalTextureSample();

    protected PortalTextureSample() {
        super(
            Window.instance.getResolution(),
            Vector2iMaths.zero,
            Window.instance.getResolution()
        );
    }
    
}
