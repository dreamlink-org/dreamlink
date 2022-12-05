package dreamlink.player.integration.strategy;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.AABBfMaths;
import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.block.DoorBlock;
import dreamlink.zone.block.IBlock;

public class DoorCollisionStrategy implements ICollisionStrategy {

    public static DoorCollisionStrategy instance = new DoorCollisionStrategy();

    @Override
    public boolean isCollision(
        IPlayerDirectory directory,
        AABBf collider, 
        Vector3i blockPosition, 
        IBlock block
    ) {
        if(Simulation.instance.simulationMode != SimulationMode.explore) {
            return false;
        }

        if(block instanceof DoorBlock) {
            var zone = directory.getPlayerState().zone;
            var door = zone.getDoorByPosition(blockPosition);
            var openDoor = zone.getOpenDoor();

            var doorCollider = door.getCollider(new AABBf());
            if(door != openDoor) {
                return doorCollider.intersectsAABB(collider);
            }

            var projection = new AABBf();
            for(var cubeFace : CubeFace.getCubeFaces()) {
                if(door.orientation.cubeFace == cubeFace) {
                    continue;
                }

                AABBfMaths.project(projection, doorCollider, cubeFace);
                if(projection.intersectsAABB(collider)) {
                    return true;
                }
            }
        }

        return false;
    }
    
}
