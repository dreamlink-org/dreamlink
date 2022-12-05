package dreamlink.graphics.texture.sample;

import org.joml.Vector2i;

public class ZoneTextureSample extends TextureSample {

    public ZoneTextureSample(
        Vector2i atlasDimensions, 
        Vector2i position, 
        Vector2i dimensions,
        Vector2i animationStride, 
        int animationTotalFrames, 
        int animationRowFrames, 
        int animationStartFrame,
        int animationSpeed
    ) {
        super(
            atlasDimensions, 
            position, 
            dimensions, 
            animationStride, 
            animationTotalFrames, 
            animationRowFrames,
            animationStartFrame, 
            animationSpeed
        );
    }

    
}
