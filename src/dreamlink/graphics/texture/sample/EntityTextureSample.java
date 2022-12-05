package dreamlink.graphics.texture.sample;

import org.joml.Vector2i;

public class EntityTextureSample extends TextureSample {

    private static Vector2i atlasDimensions = new Vector2i(512, 544);

    public static TextureSample barrier = new EntityTextureSample(new Vector2i(0, 0), new Vector2i(32, 32));
    public static TextureSample missing = new EntityTextureSample(new Vector2i(32, 0), new Vector2i(32, 32));
    public static TextureSample doorSide = new EntityTextureSample(new Vector2i(64, 0), new Vector2i(32, 32));
    public static TextureSample doorTop = new EntityTextureSample(new Vector2i(96, 0), new Vector2i(16, 16));
    public static TextureSample doorBottom = new EntityTextureSample(new Vector2i(96, 16), new Vector2i(16, 16));
    public static TextureSample logo = new EntityTextureSample(new Vector2i(0, 32), new Vector2i(512, 512));

    public EntityTextureSample(Vector2i position, Vector2i dimensions) {
        super(EntityTextureSample.atlasDimensions, position, dimensions);
    }
    
}
