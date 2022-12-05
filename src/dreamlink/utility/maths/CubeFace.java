package dreamlink.utility.maths;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3i;

public class CubeFace {

    private static Map<String, CubeFace> cubeFaceMap = new HashMap<>();
    private static Map<Integer, CubeFace> cubeFaceIDMap = new HashMap<>();

    public static CubeFace getCubeFace(String cubeFaceName) {
        return cubeFaceMap.get(cubeFaceName);
    }

    public static CubeFace getCubeFace(int cubeFaceID) {
        return cubeFaceIDMap.get(cubeFaceID);
    }

    public static Iterable<CubeFace> getCubeFaces() {
        return cubeFaceMap.values();
    }

    public static CubeFace front = new CubeFace(
        0,
        "front",
        new Vector3i(0, 0, 1),
        2
    );

    public static CubeFace left = new CubeFace(
        1,
        "left",
        new Vector3i(1, 0, 0),
        0
    );

    public static CubeFace top = new CubeFace(
        2,
        "top",
        new Vector3i(0,1,0),
        1
    );

    public static CubeFace back = new CubeFace(
        3,
        "back",
        new Vector3i(0, 0, -1),
        2
    );


    public static CubeFace right = new CubeFace(
        4,
        "right",
        new Vector3i(-1, 0, 0),
        0
    );


    public static CubeFace bottom = new CubeFace(
        5,
        "bottom",
        new Vector3i(0,-1,0),
        1
    );


    public Vector3i normal;
    public String name;
    public int cubeFaceID;
    public int axisID;

    CubeFace(int cubeFaceID, String name, Vector3i normal, int axisID) {
        this.cubeFaceID = cubeFaceID;
        this.name = name;
        this.normal = new Vector3i(normal);
        this.axisID = axisID;


        cubeFaceMap.put(name, this);
        cubeFaceIDMap.put(cubeFaceID, this);
    }

    public String toString() {
        return String.format("CubeFace(%s)", this.name);
    }

    public boolean isSameAxis(CubeFace other) {
        return this.axisID == other.axisID;
    }

    public CubeFace getOpposite() {
        var oppositeID = (this.cubeFaceID + 3) % 6;
        return getCubeFace(oppositeID);
    }

}
