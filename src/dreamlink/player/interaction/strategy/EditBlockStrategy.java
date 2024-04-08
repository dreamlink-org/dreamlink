package dreamlink.player.interaction.strategy;

import org.joml.Vector3i;

import dreamlink.menu.component.simulation.quickbar.QuickBarComponent;
import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.block.AirBlock;
import dreamlink.zone.block.BarrierBlock;
import dreamlink.zone.block.DoorBlock;
import dreamlink.zone.block.IBlock;
import dreamlink.zone.block.SpeakerBlock;
import dreamlink.zone.block.UserBlock;
import dreamlink.zone.terrain.TerrainBlockData;

public class EditBlockStrategy implements IInteractionStrategy {

    private static long applyPeriod = 100;
    private static long initialApplyPeriod = 1_000;

    private long nextApplyTime;
    private Vector3i lastApplyPosition;

    public EditBlockStrategy() {
        this.lastApplyPosition = new Vector3i();
    }

    @Override
    public boolean canInteract(
        IPlayerDirectory playerDirectory, 
        Vector3i blockPosition, 
        IBlock block
    ) {
        return Simulation.instance.simulationMode == SimulationMode.edit;
    }

    @Override
    public void interact(
        IPlayerDirectory playerDirectory, 
        Vector3i blockPosition, 
        CubeFace rayCubeFace,
        IBlock block
    ) {
        var playerState = playerDirectory.getPlayerState();
        var orientation = Orientation.fromYaw(playerState.rotation.y).getOpposite();

        if(Window.instance.isButtonDown(Button.mouseLeft)) {
            var selectedStamp = QuickBarComponent.instance.getSelectedStamp();
            var applyTime = System.currentTimeMillis();
            var applyPosition = new Vector3i(blockPosition).add(rayCubeFace.normal);
            var applyBlockID = playerState.zone.getBlockData(
                applyPosition, new TerrainBlockData()
            ).blockID;

            var isSameBlock = this.lastApplyPosition.equals(blockPosition);
            var unableToApply = isSameBlock && applyTime < this.nextApplyTime;
            if(unableToApply || selectedStamp == null || applyBlockID != AirBlock.blockID) {
                return;
            }

            selectedStamp.applyStamp(applyPosition, orientation);
            this.lastApplyPosition.set(applyPosition);

            var applyPeriod = Window.instance.isButtonPressed(Button.mouseLeft) || !isSameBlock 
                ? EditBlockStrategy.initialApplyPeriod
                : EditBlockStrategy.applyPeriod;

            this.nextApplyTime = applyTime + applyPeriod;
            
        } else if(Window.instance.isButtonPressed(Button.mouseRight)) {
            if(block instanceof BarrierBlock) {
                return;
            } else if(block instanceof DoorBlock) {
                var door = playerState.zone.getDoorByPosition(blockPosition);
                door.pickup();
            } else if(block instanceof SpeakerBlock) {
                var speaker = playerState.zone.getSpeakerByPosition(blockPosition);
                speaker.pickup(blockPosition);
            } else if(block instanceof UserBlock) {
                playerState.zone.setBlockData(blockPosition, new TerrainBlockData());
            }
        }
    }
    
}
