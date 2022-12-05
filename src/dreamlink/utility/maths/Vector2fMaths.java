package dreamlink.utility.maths;

import org.joml.Vector2f;

public class Vector2fMaths {

    public static Vector2f zero = new Vector2f(0f);
    public static Vector2f one = new Vector2f(1f);

    public static float getAxisValue(Vector2f vector, int axis) {
        if(axis == 0) {
            return vector.x;
        } else if(axis == 1) {
            return vector.y;
        } else {
            return 0;
        }
    }

    public static void setAxisValue(Vector2f vector, int axis, float value) {
        if(axis == 0) {
            vector.x = value;
        } else if(axis == 1) {
            vector.y = value;
        }
    }
    
}
