package dreamlink.player.interaction.strategy;

import org.joml.Vector3i;

import dreamlink.gamestate.simulation.SimulationMessageViewerMenuGameState;
import dreamlink.menu.component.simulation.SimulationMessageViewerComponent;
import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.CubeFace;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.block.IBlock;
import dreamlink.zone.block.UserBlock;

public class ReadBlockStrategy implements IInteractionStrategy {

    public static ReadBlockStrategy instance = new ReadBlockStrategy();

    @Override
    public boolean canInteract(
        IPlayerDirectory directory, 
        Vector3i blockPosition, 
        IBlock block
    ) {
        if(Simulation.instance.simulationMode != SimulationMode.explore) {
            return false;
        }

        if(block instanceof UserBlock userBlock) {
            return !userBlock.interactionMessage.isEmpty();
        }

        return false;
    }

    @Override
    public void interact(
        IPlayerDirectory directory, 
        Vector3i blockPosition, 
        CubeFace rayCubeFace, 
        IBlock block
    ) {
        if(!Window.instance.isButtonPressed(Button.keyE)) {
            return;
        }

        var userBlock = (UserBlock)block;
        SimulationMessageViewerComponent.instance.setMessage(userBlock.interactionMessage);
        Simulation.instance.setGameState(SimulationMessageViewerMenuGameState.instance);

    }
}
