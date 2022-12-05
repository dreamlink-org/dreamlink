package dreamlink.player.integration;

import org.joml.AABBf;
import org.joml.AABBi;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.player.integration.strategy.BarrierCollisionStrategy;
import dreamlink.player.integration.strategy.DoorCollisionStrategy;
import dreamlink.player.integration.strategy.ICollisionStrategy;
import dreamlink.player.integration.strategy.UserBlockCollisionStrategy;
import dreamlink.player.state.PlayerState;
import dreamlink.player.state.PlayerStateSnapShot;
import dreamlink.utility.maths.AABBiMaths;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.PortalTransformer;
import dreamlink.utility.maths.Vector2fMaths;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.terrain.TerrainBlockData;

public class PlayerPositionIntegrator {

    private static float stepEpsilon = 0.001f;
    private static float nudgeEpsilon = 0.001f;

    private static ICollisionStrategy[] strategies = new ICollisionStrategy[] {
        BarrierCollisionStrategy.instance,
        DoorCollisionStrategy.instance,
        UserBlockCollisionStrategy.instance
    };

    private Vector2f[] nudgeVectors = new Vector2f[] {
        Vector2fMaths.zero,
        new Vector2f(1, 0),
        new Vector2f(1, 1),
        new Vector2f(0, 1),
        new Vector2f(-1, 1),
        new Vector2f(-1, 0),
        new Vector2f(-1, -1),
        new Vector2f(0, -1),
        new Vector2f(1, -1),
    };

    private IPlayerDirectory directory;

    public PlayerPositionIntegrator(IPlayerDirectory directory) {
        this.directory = directory;
        for(var ix = 1; ix < this.nudgeVectors.length; ix += 1) {
            this.nudgeVectors[ix].mul(PlayerPositionIntegrator.nudgeEpsilon);
        }
    }

    private boolean isColliding() {
        var playerState = this.directory.getPlayerState();
        var collider = playerState.getCollider(new AABBf());
        var blockPosition = new Vector3i();
        var blockData = new TerrainBlockData();

        var collisionRange = AABBiMaths.expandFrom(new AABBi(), collider);
        for(var x = collisionRange.minX; x < collider.maxX; x += 1) {
            for(var y = collisionRange.minY; y < collider.maxY; y += 1) {
                for(var z = collisionRange.minZ; z < collider.maxZ; z += 1) {
                    playerState.zone.getBlockData(blockPosition.set(x, y, z), blockData);
                    var block = playerState.zone.getBlockByID(blockData.blockID);
                    for(var strategy : PlayerPositionIntegrator.strategies) {
                        if(strategy.isCollision(this.directory, collider, blockPosition, block)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Has the actual camera (single point), crossed the plane of the teleportation portal
    private boolean isPlayerTeleportationThresholdCrossed(PlayerState playerState) {
        var openDoor = playerState.zone.getOpenDoor();
        if(openDoor == null) {
            return false;
        }

        var doorCollider = openDoor.getCollider(new AABBf());
        var collider = playerState.getCollider(new AABBf());
        if(!doorCollider.intersectsAABB(collider)) {
            return false;
        }

        var planePosition = openDoor.getPlanePosition(new Vector3f());
        var relativeDistance = playerState.getHeadPosition(new Vector3f())
            .sub(planePosition);
        var doorNormal = openDoor.orientation.cubeFace.normal;
        return Vector3fMaths.dot(relativeDistance, doorNormal) < 0;
    }

    // Perform a teleportation of the player from one portal to another.
    // N.B. its crucial to ALWAYS keep the door/level pairs in sync. If you want to
    // be cheeky and just modify the current level and door, working out the linked
    // level and door later - you'll run into problems because until that work is done
    // the linked door and current door will be the same - screwing up teleportation
    // transformations. 
    private void teleport(PlayerState playerState) {
        var derivedOrigin = playerState.getHeadPosition(new Vector3f()).sub(playerState.position);
        var openDoor = playerState.zone.getOpenDoor();
        var targetZone = openDoor.resolve();
        var targetDoor = targetZone.getDoorByName(openDoor.targetDoorName);

        // First, lets swap the level.
        playerState.zone = targetZone;

        // Okay, now lets transform our position, velocity and rotation.
        // N.B. we need to  make sure the rotation happens about the head position,
        // which is the position + the origin - thus add the origin pre-teleport and
        // subtract it post-teleport.
        var transformer = new PortalTransformer(
            openDoor.getPlanePosition(new Vector3f()),
            openDoor.orientation.yaw,
            targetDoor.getPlanePosition(new Vector3f()),
            targetDoor.orientation.yaw
        );

        playerState.position.add(derivedOrigin);
        transformer.getTransformedPosition(playerState.position);
        transformer.getTransformedRotation(playerState.rotation);
        transformer.getTransformedVelocity(playerState.velocity);
        playerState.position.sub(derivedOrigin);
    }

    private void updateCrouching() {
        var playerState = this.directory.getPlayerState();

        // Take a snapshot of the player's core state before any mutation - so we can revert if necessary.
        var snapShot = playerState.getSnapShot(new PlayerStateSnapShot());

        // Apply the player's desired crouching state - N.B. this will affect the player's dimensions.
        playerState.isCrouching = Window.instance.isButtonDown(Button.keyLeftCtrl);

        // Bummer! By changing our crouch state we've caused a collision with the terrain.
        // Revert the state and abort.
        if(this.isColliding()) {
            playerState.restoreFromSnapShot(snapShot);
            return;
        }
    }

    private void updatePosition() {
        var playerState = this.directory.getPlayerState();

        // Create re-usable temporary working variables to avoid unnecessary allocations...
        var snapShot = new PlayerStateSnapShot();
        var deltaStep = new Vector3f();
        var adjustmentVector = new Vector3f();

        // If we take tiny steps of length epsilon, how many steps do we need to trace the
        // entire length of the velocity vector?
        var velocityMagnitude = playerState.velocity.distance(Vector3fMaths.zero);
        var numSteps = (int) Math.ceil(velocityMagnitude / stepEpsilon);

        // Initially assume we're not colliding with anything.
        playerState.clearContacts();

        // Start walking along the velocity vector in tiny steps.
        for(var ix = 0; ix < numSteps; ix += 1) {
            // We don't want walls to be sticky - i.e. if we glance them we don't
            // want to come to a complete stop. To accomplish this, we further break down
            // each tiny delta step into its component parts along each axis.

            // If we detect a collision along a specific axis, we can zero the velocity
            // *ONLY* in that axis. This will allow us to continue moving along the other
            // axes.
            for(var cubeFace : CubeFace.getCubeFaces()) {
                // Create a vector to represent a "step". It will be the velocity vector scaled down
                // to have magnitude epsilon.
                deltaStep.set(playerState.velocity).div(numSteps);

                // We split the delta step into axis by iterating through cube faces. There
                // are 6 cube faces, but 3 axes. Thus, in order to not double count, we
                // exclude the cube faces pointing in the opposite direction to travel.
                var dot = Vector3fMaths.dot(deltaStep, cubeFace.normal);
                if(dot <= 0) {
                    continue;
                }

                // Get the magnitude of the step along the current axis.
                var deltaAxis = Vector3fMaths.getAxisValue(deltaStep, cubeFace.axisID);

                // Take a snapshot of our state before any mutation - so we can revert if necessary.
                // All the captured variables below have the possibility of being mutated
                // as a result of our movement!
                playerState.getSnapShot(snapShot);

                // Assume we have collided unless told otherwise.
                var isCollision = true;

                // Due to floating point inaccuracies, (exacerbated by the fact that we can
                // teleport large distances around the map, we might find that we suddenly
                // ever-so-slightly collide with the terrain. To overcome this, before we
                // resign ourselves to having colldied, we first try adjusting the position
                // by nudging the player in various directions orthogonal to the direction
                // of travel. The first nudge vector is (0, 0) - i.e. no nudge, if this fails
                // we then revert to non-zero nudges.
                for(var nudgeVector : this.nudgeVectors) {
                    // Construct the "adjustmentVector", which is the combination of the
                    // *component* of the deltaStep and whatever nudge we're applying.
                    for(var nudgeIndex = 0; nudgeIndex < 2; nudgeIndex += 1) {
                        // We want to nudge in directions orthogonal to the direction of travel.
                        // If the current axis is Q, where Q is (0, 1, 2), then orthogonal axes
                        // would be Q + 1 and Q + 2 (modulo 3).
                        var adjustmentAxis = (cubeFace.axisID + nudgeIndex + 1) % 3;
                        var nudgeAxisValue = Vector2fMaths.getAxisValue(nudgeVector, nudgeIndex);
                        Vector3fMaths.setAxisValue(adjustmentVector, adjustmentAxis, nudgeAxisValue);
                    }

                    // We've now set every axis of the adjustmentVector - except for the
                    // axis of travel - we do this now. This axis is necessarily *not* either of
                    // the adjustment axes that were just set.
                    Vector3fMaths.setAxisValue(adjustmentVector, cubeFace.axisID, deltaAxis);

                    // Apply the adjustment vector to the player's position - A.K.A. actually
                    // make the player move!
                    playerState.position.add(adjustmentVector);

                    // If we've collided, then revert our mutation and try again with
                    // another nudge vector.
                    if(this.isColliding()) {
                        playerState.restoreFromSnapShot(snapShot);
                        continue;
                    }

                    if(
                        true
                        && playerState.zone.getOpenDoor() != null
                        && this.isPlayerTeleportationThresholdCrossed(playerState)
                    ) {
                        this.teleport(playerState);
                    }

                    // If we've made it this far, we know for certain that this component of the delta step 
                    // (plus any nudging) is safe. Note this and abort - we have no need to examine other potential
                    // nudges.
                    isCollision = false;
                    break;
                }

                // We reach this point by either finding a safe path forward with/without a nudge and exiting the loop early,
                // or we exhaust all possible nudges and still can't find a way to move without colliding.
                // If we ultimately do collide, then we need to zero the velocity in the axis of travel.
                // Also don't forget to zero the delta step - I don't think this is strictly necessary but it will save
                // a bunch of compute time as the dot product will be zero and the axis will be skipped entirely for future steps.
                // Finally, tag that we've touched the ground if we've collided when moving vertically downwards.
                if(isCollision) {
                    Vector3fMaths.setAxisValue(playerState.velocity, cubeFace.axisID, 0);
                    playerState.isInContact(cubeFace, true);
                }
            }
        }
    }

    public void update() {
        this.updateCrouching();
        this.updatePosition();
    }
    
}
