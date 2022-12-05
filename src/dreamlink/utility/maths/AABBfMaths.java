package dreamlink.utility.maths;

import org.joml.AABBf;

public class AABBfMaths {

    public static AABBf set(AABBf target, AABBf other) {
        target.minX = other.minX;
        target.minY = other.minY;
        target.minZ = other.minZ;
        target.maxX = other.maxX;
        target.maxY = other.maxY;
        target.maxZ = other.maxZ;
        return target; 
    }

    public static AABBf project(AABBf target, AABBf other, CubeFace cubeFace) {
        AABBfMaths.set(target, other);
        if(cubeFace.normal.x != 0) {
            target.minX = target.maxX = cubeFace.normal.x > 0
                ? other.maxX
                : other.minX;
        }

        if(cubeFace.normal.y != 0) {
            target.minY = target.maxY = cubeFace.normal.y > 0
                ? other.maxY
                : other.minY;
        }

        if(cubeFace.normal.z != 0) {
            target.minZ = target.maxZ = cubeFace.normal.z > 0
                ? other.maxZ
                : other.minZ;
        }

        return target;
    }
    
}
