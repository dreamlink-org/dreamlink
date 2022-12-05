package dreamlink.utility.maths;

import org.joml.Vector2i;

public class Vector2iMaths {

    public static Vector2i zero = new Vector2i(0, 0);

    public static Vector2i div(Vector2i vector, Vector2i divisor) {
        return vector.set(vector.x / divisor.x, vector.y / divisor.y);
    }

    public static Vector2i div(Vector2i vector, int divisor) {
        return vector.set(vector.x / divisor, vector.y / divisor);
    }

    public static int getAxisValue(Vector2i vector, int axis) {
        if(axis == 0) {
            return vector.x;
        } else if(axis == 1) {
            return vector.y;
        } else {
            return 0;
        }
    }

    public static void setAxisValue(Vector2i vector, int axis, int value) {
        if(axis == 0) {
            vector.x = value;
        } else if(axis == 1) {
            vector.y = value;
        }
    }
    
}
