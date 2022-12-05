package dreamlink.player.state;

import org.joml.AABBf;
import org.joml.Vector3f;

import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.zone.Zone;

public class PlayerState {

    private static float epsilon = 0.001f;
    private static String mainDoor = "main";
    private static Vector3f standingDimensions = new Vector3f(0.5f, 1.8f, 0.5f);
    private static Vector3f crouchingDimensions = new Vector3f(0.5f, 0.9f, 0.5f);
    private static Vector3f editDimensions = new Vector3f(0.5f);
    private static Vector3f originFactor = new Vector3f(0.5f, 0.9f, 0.5f);

    public Zone zone;
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f velocity;
    public boolean isCrouching;
    private boolean[] isInContact = new boolean[6];

    public long lastSwimTime;
    public boolean isSwimming;

    public PlayerState() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.velocity = new Vector3f();
    }

    public void spawn() {
        if(Simulation.instance.simulationMode == SimulationMode.explore) {
            var door = this.zone.getDoorByName(PlayerState.mainDoor);
            door.getPlanePosition(this.position);
            Vector3fMaths.add(this.position, door.orientation.cubeFace.normal);
            this.position.y -= 1f - PlayerState.epsilon;
            this.rotation.y = door.orientation.yaw;
        } else {
            this.position.set(this.zone.getDimensions()).mul(0.5f);
        }
    }

    public PlayerStateSnapShot getSnapShot(PlayerStateSnapShot snapshot) {
        snapshot.zone = this.zone;
        snapshot.position.set(this.position);
        snapshot.rotation.set(this.rotation);
        snapshot.velocity.set(this.velocity);
        snapshot.isCrouching = this.isCrouching;
        snapshot.isSwimming = this.isSwimming;
        snapshot.lastSwimTime = this.lastSwimTime;
        for(var cubeFace : CubeFace.getCubeFaces()) {
            snapshot.isContact(cubeFace, this.isInContact[cubeFace.cubeFaceID]);
        }
        return snapshot;
    }

    public void restoreFromSnapShot(PlayerStateSnapShot snapshot) {
        this.zone = snapshot.zone;
        this.position.set(snapshot.position);
        this.rotation.set(snapshot.rotation);
        this.velocity.set(snapshot.velocity);
        this.isCrouching = snapshot.isCrouching;
        this.lastSwimTime = snapshot.lastSwimTime;
        this.isSwimming = snapshot.isSwimming;
        for(var cubeFace : CubeFace.getCubeFaces()) {
            this.isInContact[cubeFace.cubeFaceID] = snapshot.isContact(cubeFace);
        }
    }

    private Vector3f getDimensions() {
        if(Simulation.instance.simulationMode == SimulationMode.edit) {
            return PlayerState.editDimensions;
        } else {
            return this.isCrouching
                ? PlayerState.crouchingDimensions
                : PlayerState.standingDimensions;
        }
    }

    public boolean isInContact(CubeFace face) {
        return this.isInContact[face.cubeFaceID];
    }

    public void isInContact(CubeFace face, boolean value) {
        this.isInContact[face.cubeFaceID] = value;
    }

    public void clearContacts() {
        for(var i = 0; i < this.isInContact.length; i++) {
            this.isInContact[i] = false;
        }
    }

    public Vector3f getHeadPosition(Vector3f headPosition) {
        if(Simulation.instance.simulationMode == SimulationMode.edit) {
            return headPosition
                .set(PlayerState.editDimensions)
                .mul(0.5f)
                .add(this.position);
        }

        return headPosition
            .set(this.getDimensions())
            .mul(PlayerState.originFactor)
            .add(this.position);
    }

    public AABBf getCollider(AABBf collider) {
        var dimensions = this.getDimensions();
        return collider.setMin(
            this.position.x, 
            this.position.y, 
            this.position.z
        ).setMax(
            this.position.x + dimensions.x,
            this.position.y + dimensions.y,
            this.position.z + dimensions.z
        );
    }
    
}
