package dreamlink.graphics.texture.sample;

import dreamlink.Config;
import dreamlink.utility.maths.Vector2iMaths;

public class PortalTextureSample extends TextureSample {

    public static PortalTextureSample instance = new PortalTextureSample();

    protected PortalTextureSample() {
        super(
            Config.instance.resolution,
            Vector2iMaths.zero,
            Config.instance.resolution
        );
    }
    
}
