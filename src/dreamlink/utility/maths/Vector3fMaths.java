package dreamlink.utility.maths;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class Vector3fMaths {

    public static Vector3f zero = new Vector3f(0f);
    public static Vector3f one = new Vector3f(1f);

    public static Vector3f fuzz(Vector3f vector, float epsilon) {
        vector.x = FloatMaths.fuzz(vector.x, epsilon);
        vector.y = FloatMaths.fuzz(vector.y, epsilon);
        vector.z = FloatMaths.fuzz(vector.z, epsilon);
        return vector;
    }

    public static float dot(Vector3f a, Vector3i b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3f add(Vector3f vector, Vector3i other) {
        return vector.add(other.x, other.y, other.z);
    }

    public static Vector3f directionFromRotation(Vector3f direction, Vector3f rotation) {
        return direction.set(0, 0, 1).rotateX(rotation.x).rotateY(rotation.y);
    }

    public static float distance(Vector3f vector, Vector3i other) {
        var dx = vector.x - other.x;
        var dy = vector.y - other.y;
        var dz = vector.z - other.z;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3f safeNormalize(Vector3f vector, float epsilon) {
        var magnitude = vector.length();
        return vector.set(
            vector.x / (magnitude + epsilon),
            vector.y / (magnitude + epsilon),
            vector.z / (magnitude + epsilon)
        );
    }

    public static float getAxisValue(Vector3f vector, int axis) {
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

    public static void setAxisValue(Vector3f vector, int axis, float value) {
        if(axis == 0) {
            vector.x = value;
        } else if(axis == 1) {
            vector.y = value;
        } else if(axis == 2) {
            vector.z = value;
        }
    }

    public static Vector3f convertHSVToRGB(Vector3f rgb, Vector3f hsv) {
        var i = (int)Math.floor(hsv.x * 6);
        var f = hsv.x * 6 - i;
        var p = hsv.z * (1 - hsv.y);
        var q = hsv.z * (1 - f * hsv.y);
        var t = hsv.z * (1 - (1 - f) * hsv.y);
        switch (i % 6) {
            case 0:
                return rgb.set(hsv.z, t, p);
            case 1:
                return rgb.set(q, hsv.z, p);
            case 2:
                return rgb.set(p, hsv.z, t);
            case 3:
                return rgb.set(p, q, hsv.z);
            case 4:
                return rgb.set(t, p, hsv.z);
            case 5:
                return rgb.set(hsv.z, p, q);
            default:
                return rgb.zero();
        }
    }

    
}
