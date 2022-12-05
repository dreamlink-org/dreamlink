package dreamlink.utility;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.json.JSONArray;

public class JSONFns {

    public static JSONArray getJSONFromVector4f(Vector4f vector) {
        var array = new JSONArray();
        array.put(vector.x);
        array.put(vector.y);
        array.put(vector.z);
        array.put(vector.w);
        return array;
    }

    public static JSONArray getJSONFromVector3f(Vector3f vector) {
        var array = new JSONArray();
        array.put(vector.x);
        array.put(vector.y);
        array.put(vector.z);
        return array;
    }

    public static JSONArray getJSONFromVector3i(Vector3i vector) {
        var array = new JSONArray();
        array.put(vector.x);
        array.put(vector.y);
        array.put(vector.z);
        return array;
    }

    public static JSONArray getJSONFromVector2i(Vector2i vector) {
        var array = new JSONArray();
        array.put(vector.x);
        array.put(vector.y);
        return array;
    }

    public static Vector4f getVector4fFromJSON(Vector4f target, JSONArray array) {
        return target.set(
            array.getFloat(0),
            array.getFloat(1),
            array.getFloat(2),
            array.getFloat(3)
        );
    }

    public static Vector3f getVector3fFromJSON(Vector3f target, JSONArray array) {
        return target.set(
            array.getFloat(0),
            array.getFloat(1),
            array.getFloat(2)
        );
    }

    public static Vector3i getVector3iFromJSON(Vector3i target, JSONArray array) {
        return target.set(
            array.getInt(0),
            array.getInt(1),
            array.getInt(2)
        );
    }

    public static Vector2i getVector2iFromJSON(Vector2i target, JSONArray array) {
        return target.set(
            array.getInt(0),
            array.getInt(1)
        );
    }
    
}
