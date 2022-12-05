package dreamlink.player.interaction.strategy;

import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.block.IBlock;

public interface IInteractionStrategy {

    public boolean canInteract(
        IPlayerDirectory directory, 
        Vector3i blockPosition, 
        IBlock block
    );

    public void interact(
        IPlayerDirectory directory, 
        Vector3i blockPosition, 
        CubeFace rayCubeFace,
        IBlock block
    );
    
}
