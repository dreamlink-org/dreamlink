package dreamlink.player.integration.strategy;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.zone.block.BarrierBlock;
import dreamlink.zone.block.IBlock;

public class BarrierCollisionStrategy implements ICollisionStrategy {

    public static BarrierCollisionStrategy instance = new BarrierCollisionStrategy();

    @Override
    public boolean isCollision(
        IPlayerDirectory directory,
        AABBf collider,
        Vector3i blockPosition,
        IBlock block
    ) {
        if(block instanceof BarrierBlock) {
            return true;
        }

        return false;
    }
    
}
