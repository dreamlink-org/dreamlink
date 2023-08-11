package doors.utility;

import java.util.HashMap;
import java.util.Map;

import doors.utility.vector.Vector3in;
import doors.utility.vector.Vector3fl;

public class CubeFace {

    public static Map<String, CubeFace> CUBE_FACE_MAP = new HashMap<>();

    public static CubeFace FRONT = new CubeFace(
        "front",
        new Vector3in(0, 0, 1),
        new Vector3fl(0f, 0f, 0f)
    );

    public static CubeFace BACK = new CubeFace(
        "back",
        new Vector3in(0, 0, -1),
        new Vector3fl(0f, 1f, 0f).mul(Math.PI)
    );

    public static CubeFace LEFT = new CubeFace(
        "left",
        new Vector3in(1, 0, 0),
        new Vector3fl(0f, -0.5f, 0f).mul(Math.PI)
    );

    public static CubeFace RIGHT = new CubeFace(
        "right",
        new Vector3in(-1, 0, 0),
        new Vector3fl(0f, 0.5f, 0f).mul(Math.PI)
    );

    public static CubeFace TOP = new CubeFace(
        "top",
        new Vector3in(0,1,0),
        new Vector3fl(0.5f, 0f, 0f).mul(Math.PI)
    );

    public static CubeFace BOTTOM = new CubeFace(
        "bottom",
        new Vector3in(0,-1,0),
        new Vector3fl(-0.5f, 0f, 0f).mul(Math.PI)
    );

    public static CubeFace[] CUBE_FACES = {
        FRONT, LEFT, TOP, BACK, RIGHT, BOTTOM
    };

    public Vector3in normal;
    public Vector3fl rotation;
    public String name;
    private int index = -1;

    CubeFace(String name, Vector3in normal, Vector3fl rotation) {
        this.name = name;
        this.rotation = rotation;
        this.normal = normal;
        CUBE_FACE_MAP.put(name, this);
    }

    public int getIndex() {
        if(this.index == -1) {
            for(var i = 0; i < CUBE_FACES.length; i++) {
                if(CUBE_FACES[i] == this) {
                    this.index = i;
                    break;
                }
            }
        }
        return this.index;
    }

    public int getFlag() {
        return 1 << this.getIndex();
    }

    public CubeFace getOpposite() {
        var index = (this.getIndex() + 3) % 6;
        return CUBE_FACES[index];
    }

    public String toString() {
        return String.format("CubeFace(%s)", this.name);
    }

    public boolean checkFlag(int flags) {
        return (flags & this.getFlag()) != 0;
    }

}
