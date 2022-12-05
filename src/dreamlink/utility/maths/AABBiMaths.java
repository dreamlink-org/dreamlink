package dreamlink.utility.maths;

import org.joml.AABBf;
import org.joml.AABBi;

public class AABBiMaths {

    public static AABBi expandFrom(AABBi target, AABBf collider) {
        return target.setMin(
            (int)Math.floor(collider.minX),
            (int)Math.floor(collider.minY),
            (int)Math.floor(collider.minZ)
        ).setMax(
            (int)Math.ceil(collider.maxX),
            (int)Math.ceil(collider.maxY),
            (int)Math.ceil(collider.maxZ)
        );
    }
    
}
