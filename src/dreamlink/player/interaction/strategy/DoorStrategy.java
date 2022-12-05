package dreamlink.player.interaction.strategy;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.CubeFace;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.block.DoorBlock;
import dreamlink.zone.block.IBlock;

public class DoorStrategy implements IInteractionStrategy {

    public static DoorStrategy instance = new DoorStrategy();

    @Override
    public boolean canInteract(
        IPlayerDirectory directory, 
        Vector3i blockPosition, 
        IBlock block
    ) {
        if(Simulation.instance.simulationMode != SimulationMode.explore) {
            return false;
        }

        if(block instanceof DoorBlock) {
            var playerState = directory.getPlayerState();
            var door = playerState.zone.getDoorByPosition(blockPosition);
            var targetZone = door.resolve();

            if(targetZone == null || !targetZone.isReady()) {
                return false;
            }

            var targetDoor = targetZone.getDoorByName(door.targetDoorName);
            if(targetDoor == null || !targetDoor.isPlaced) {
                return false;
            }

            if(!targetDoor.targetZoneName.equals(playerState.zone.name)) {
                return false;
            }

            if(!targetDoor.targetDoorName.equals(door.name)) {
                return false;
            }

            return true;
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

        var playerState = directory.getPlayerState();
        var openDoor = playerState.zone.getOpenDoor();

        if(openDoor != null) {
            var doorCollider = openDoor.getCollider(new AABBf());
            var playerCollider = playerState.getCollider(new AABBf());
            if(doorCollider.intersectsAABB(playerCollider)) {
                return;
            }
        }

        var door = playerState.zone.getDoorByPosition(blockPosition);
        var targetDoor = door.resolve().getDoorByName(door.targetDoorName);

        if(door == openDoor) {
            door.close();
            targetDoor.close();
        } else {
            door.open();
            targetDoor.open();
        }
    }
}
