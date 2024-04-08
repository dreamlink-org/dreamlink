package dreamlink.utility.maths;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class Vector3iMaths {

    public static Vector3i zero = new Vector3i(0);
    public static Vector3i one = new Vector3i(1);

    public static int getVolume(Vector3i vector) {
        return vector.x * vector.y * vector.z;
    }

    public static int dot(Vector3i a, Vector3i b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static int getAxisValue(Vector3i vector, int axis) {
        if(axis == 0) {
            return vector.x;
        } else if(axis == 1) {
            return vector.y;
        } else if(axis == 2) {
            return vector.z;
        } else {
            return 0;
        }
    }

    public static void setAxisValue(Vector3i vector, int axis, int value) {
        if(axis == 0) {
            vector.x = value;
        } else if(axis == 1) {
            vector.y = value;
        } else if(axis == 2) {
            vector.z = value;
        }
    }

    public static Vector3i div(Vector3i target, Vector3i divisor) {
        return target.set(
            target.x / divisor.x,
            target.y / divisor.y,
            target.z / divisor.z
        );
    }

    public static Vector3i mod(Vector3i target, Vector3i divisor) {
        return target.set(
            target.x % divisor.x,
            target.y % divisor.y,
            target.z % divisor.z
        );
    }

    public static Vector3i unpack(Vector3i target, int index, Vector3i dimensions) {
        var x = index % dimensions.x;
        index /= dimensions.x;
        var z = index % dimensions.z;
        index /= dimensions.z;
        var y = index;
        return target.set(x, y, z);
    }

    public static Vector3i castFrom(Vector3i target, Vector3f vector) {
        return target.set(
            (int)vector.x,
            (int)vector.y,
            (int)vector.z
        );
    }

    public static int pack(Vector3i vector, Vector3i dimensions) {
        var packedValue = 0;
        packedValue += vector.y * dimensions.x * dimensions.z;
        packedValue += vector.z * dimensions.x; 
        packedValue += vector.x;
        return packedValue;
    }

    
}
