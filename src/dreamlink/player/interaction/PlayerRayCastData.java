package dreamlink.player.interaction;

import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.block.IBlock;

public class PlayerRayCastData {

    public boolean isHit;
    public CubeFace rayCubeFace;
    public Vector3i blockPosition;
    public Vector3f rayDirection;
    public Vector3f rayTerminationPosition;
    public IBlock block;

    public PlayerRayCastData(Vector3f rayStartPosition, Vector3f rayDirection) {
        this.rayCubeFace = CubeFace.front;
        this.rayDirection = new Vector3f(rayDirection);
        this.rayTerminationPosition = new Vector3f(rayStartPosition);
        this.blockPosition = new Vector3i(
            (int)rayStartPosition.x,
            (int)rayStartPosition.y,
            (int)rayStartPosition.z
        );
    }

}
