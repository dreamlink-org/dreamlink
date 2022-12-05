package dreamlink.player.integration.strategy;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.zone.block.IBlock;

public interface ICollisionStrategy {

    public boolean isCollision(
        IPlayerDirectory directory,
        AABBf collider,
        Vector3i blockPosition,
        IBlock block
    );
    
}
