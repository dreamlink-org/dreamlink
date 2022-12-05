package dreamlink.player.state;

import org.joml.Vector3f;

import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.Zone;

public class PlayerStateSnapShot {

    public Zone zone;
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f velocity;
    public boolean isCrouching;
    private boolean[] isContactFaces;

    public long lastSwimTime;
    public boolean isSwimming;

    public PlayerStateSnapShot() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.velocity = new Vector3f();
        this.isContactFaces = new boolean[6];
    }

    public void isContact(CubeFace cubeFace, boolean value) {
        this.isContactFaces[cubeFace.cubeFaceID] = value;
    }

    public boolean isContact(CubeFace cubeFace) {
        return this.isContactFaces[cubeFace.cubeFaceID];
    }

}
