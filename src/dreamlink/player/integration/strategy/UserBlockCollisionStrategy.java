package dreamlink.player.integration.strategy;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.zone.block.BlockMaterial;
import dreamlink.zone.block.IBlock;
import dreamlink.zone.block.UserBlock;

public class UserBlockCollisionStrategy implements ICollisionStrategy {

    public static UserBlockCollisionStrategy instance = new UserBlockCollisionStrategy();

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

        if(block instanceof UserBlock userBlock) {
            return userBlock.blockMaterial == BlockMaterial.solid;
        }

        return false;
    }
    
}
