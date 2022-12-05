package dreamlink.player.interaction;

import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.player.interaction.strategy.DoorStrategy;
import dreamlink.player.interaction.strategy.EditBlockStrategy;
import dreamlink.player.interaction.strategy.IInteractionStrategy;
import dreamlink.player.interaction.strategy.ReadBlockStrategy;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.utility.maths.Vector3iMaths;
import dreamlink.zone.block.IBlock;
import dreamlink.zone.terrain.TerrainBlockData;

public class PlayerInteractionSystem {

    private class InternalRayData {

        public CubeFace rayCubeFace;
        public Vector3i blockPosition;
        public Vector3f rayPosition;

        public InternalRayData(Vector3f rayPosition) {
            this.rayCubeFace = CubeFace.front;
            this.rayPosition = new Vector3f(rayPosition);
            this.blockPosition = Vector3iMaths.castFrom(new Vector3i(), rayPosition);
        }

        public void advance(Vector3f rayDirection) {
            var minDistance = Float.MAX_VALUE; 
            var minFace = CubeFace.front;

            for(var cubeFace : CubeFace.getCubeFaces()) {

                // Get the sign of the normal (+1) or (-1).
                var normalSignum = Vector3fMaths.dot(Vector3fMaths.one, cubeFace.normal);

                // Check to see if the ray is going in the same direction as the normal.
                // If its not, then the ray is going in the wrong direction and we
                // should skip this face.
                var projection = Vector3fMaths.dot(rayDirection, cubeFace.normal);
                if(projection <= 0) {
                    continue;
                }

                // We are now filtering out half the faces - and will be left with
                // one normal for the X axis, one for the Y and one for the Z.

                // Get the current position of the ray along the axis.
                var positionProjection = Vector3fMaths.dot(this.rayPosition, cubeFace.normal) * normalSignum;

                // Get the distance from the current position to the boundary of the next voxel.
                // We use the cursor to "quantize" our current position and tell us which voxel we're in.
                // You would think this could be inferred from the position with some rounding but
                // unfortunately there is a problem. Consider the scenario where we are in voxel (x,y,z) and
                // our position is (x,q,w). How far do we need to move until we reach the voxel (x-1, y, z)?
                // The answer is 0, but if we move 0, then the position won't change. Therefore, for certain
                // position values, the voxel we are in is ambigious and therefore needs to be tracked
                // separately using the cursor object.
                var shiftedPositionProjection = Vector3iMaths.dot(cubeFace.normal, this.blockPosition) * normalSignum;
                shiftedPositionProjection += normalSignum > 0 ? 1 : 0;
                var distanceToVoxelBoundary = Math.abs(shiftedPositionProjection - positionProjection);

                // Now work out how far we'd have to travel in the direction of the ray
                // to reach the boundary of the next voxel.
                var distanceAlongDirection = distanceToVoxelBoundary / projection;

                // We want to find the smallest distance, so that we don't accidentally
                // skip over a voxel.
                if(distanceAlongDirection < minDistance) {
                    minDistance = distanceAlongDirection;
                    minFace = cubeFace;
                }
            }

            this.blockPosition.add(minFace.normal);
            this.rayCubeFace = minFace.getOpposite();
            this.rayPosition.add(
                rayDirection.x * minDistance,
                rayDirection.y * minDistance,
                rayDirection.z * minDistance
            );
        }
    }

    private static int numIterations = 50;

    private static IInteractionStrategy[] strategies = new IInteractionStrategy[] {
        EditBlockStrategy.instance,
        DoorStrategy.instance,
        ReadBlockStrategy.instance
    };

    private IPlayerDirectory directory;
    public boolean canInteract;

    public PlayerInteractionSystem(IPlayerDirectory directory) {
        this.directory = directory;
    }

    public void update() {
        var playerState = this.directory.getPlayerState();

        var headPosition = playerState.getHeadPosition(new Vector3f());
        var rayData = new InternalRayData(headPosition);
        var rayDirection = Vector3fMaths.directionFromRotation(new Vector3f(), playerState.rotation);
        var isHit = false;
        var blockData = new TerrainBlockData();
        var zone = playerState.zone;
        var block = (IBlock)null;
        var collider = new AABBf();
        this.canInteract = false;

        for(var ix = 0; ix < PlayerInteractionSystem.numIterations; ix += 1) {
            zone.getBlockData(rayData.blockPosition, blockData);
            block = zone.getBlockByID(blockData.blockID);
            block.getCollider(collider, rayData.blockPosition);

            if(Intersectionf.intersectRayAab(
                headPosition.x, headPosition.y, headPosition.z,
                rayDirection.x, rayDirection.y, rayDirection.z,
                collider.minX, collider.minY, collider.minZ,
                collider.maxX, collider.maxY, collider.maxZ,
                new Vector2f() // Stores the intersection points (we don't need them)
            )) {
                isHit = true;
                break;
            }

            rayData.advance(rayDirection);
        }

        if(!isHit) {
            return;
        }

        for(var strategy : PlayerInteractionSystem.strategies) {
            if(strategy.canInteract(
                this.directory, 
                rayData.blockPosition, 
                block
            )) {
                this.canInteract = true;
                strategy.interact(
                    this.directory, 
                    rayData.blockPosition, 
                    rayData.rayCubeFace, 
                    block
                );
            }
        }
        
    }
    
}
