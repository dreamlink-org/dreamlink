package dreamlink.utility.maths;

import java.util.HashMap;
import java.util.Map;

public class Orientation {

    private static float deg360 = (float)Math.PI * 2f;
    private static float deg90 = (float)Math.PI / 2f;
    private static float deg45 = (float)Math.PI / 4f;

    private static Map<String, Orientation> orientationMap = new HashMap<>();
    private static Map<Integer, Orientation> orientationIdMap = new HashMap<>();
    private static Map<CubeFace, Orientation> orientationCubeFaceMap = new HashMap<>();

    public static Orientation getOrientation(String orientationName) {
        return orientationMap.get(orientationName);
    }

    public static Iterable<Orientation> getOrientations() {
        return orientationMap.values();
    }

    public static Orientation getOrientation(int orientationID) {
        return orientationIdMap.get(orientationID);
    }

    public static Orientation fromYaw(float yaw) {
        yaw = FloatMaths.floatMod(yaw + deg45 , deg360);
        return Orientation.getOrientation((int)Math.floor(yaw / deg90));
    }

    public static Orientation front = new Orientation(0, 0f, "front", CubeFace.front);
    public static Orientation left = new Orientation(1, (float)Math.PI / 2f, "left", CubeFace.left);
    public static Orientation back = new Orientation(2, (float)Math.PI, "back", CubeFace.back);
    public static Orientation right = new Orientation(3, (float)Math.PI / 2f * 3f, "right", CubeFace.right);

    public int orientationID;
    public String name;
    public CubeFace cubeFace;
    public float yaw;

    public Orientation(int orientationID, float yaw, String name, CubeFace cubeFace) {
        this.orientationID = orientationID;
        this.yaw = yaw;
        this.name = name;
        this.cubeFace = cubeFace;

        orientationMap.put(name, this);
        orientationIdMap.put(orientationID, this);
        orientationCubeFaceMap.put(cubeFace, this);
    }

    public Orientation getOpposite() {
        var oppositeID = (this.orientationID + 2) % 4;
        return getOrientation(oppositeID);
    }

    public Orientation getOrthogonal() {
        var orthogonalID = (this.orientationID + 1) % 4;
        return getOrientation(orthogonalID);
    }

    // Remap an "apparent" cube face to the "real" cube face. I.e. a "LEFT" cubeFace, when
    // viewed from the "BACK" orientation, is actually a "RIGHT" cube face.
    public CubeFace remap(CubeFace cubeFace) {
        var orientation = orientationCubeFaceMap.get(cubeFace);

        if(orientation == null) {
            return cubeFace;
        }

        var orientationID = 4
            + orientation.orientationID
            - this.orientationID;

        return getOrientation(orientationID % 4).cubeFace;
    }

    public String toString() {
        return String.format("Orientation(%s)", this.name);
    }

    
}
