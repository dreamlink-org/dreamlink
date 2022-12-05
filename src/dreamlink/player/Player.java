package dreamlink.player;

import org.joml.Vector3f;

import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.player.integration.PlayerPositionIntegrator;
import dreamlink.player.interaction.PlayerInteractionSystem;
import dreamlink.player.state.PlayerState;
import dreamlink.player.velocity.PlayerVelocityInputController;
import dreamlink.simulation.Simulation;
import dreamlink.zone.Zone;
import dreamlink.zone.ZoneStatus;

public class Player {

    private class InternalPlayerDirectory implements IPlayerDirectory {

        @Override
        public PlayerState getPlayerState() {
            return Player.this.state;
        }

    }

    public static Player instance = new Player();

    private PlayerState state;
    private PlayerRotationInputController rotationInputController;
    private PlayerVelocityInputController velocityController;
    private PlayerPositionIntegrator positionIntegrator;
    private PlayerInteractionSystem interactionSystem;
    private PlayerSwimChecker swimChecker;

    public Player() {
        var directory = new InternalPlayerDirectory();

        this.state = new PlayerState();
        this.rotationInputController = new PlayerRotationInputController(directory);
        this.velocityController = new PlayerVelocityInputController(directory);
        this.positionIntegrator = new PlayerPositionIntegrator(directory);
        this.interactionSystem = new PlayerInteractionSystem(directory);
        this.swimChecker = new PlayerSwimChecker(directory);
    }

    public Zone getZone() {
        return this.state.zone;
    }

    public Vector3f getHeadPosition(Vector3f headPosition) {
        return this.state.getHeadPosition(headPosition);
    }

    public Vector3f getRotation() {
        return this.state.rotation;
    }

    public boolean canInteract() {
        return this.interactionSystem.canInteract;
    }

    public void setZone(Zone zone) {
        this.state.zone = zone;
    }

    public void spawn() {
        this.state.spawn();
    }

    public void update() {
        if(Simulation.instance.getGameState() != SimulationGameState.instance) {
            return;
        }

        if(this.state.zone != null && this.state.zone.getZoneStatus() == ZoneStatus.ready) {
            this.rotationInputController.update();
            this.velocityController.updateVelocity();
            this.positionIntegrator.update();
            this.interactionSystem.update();
            this.swimChecker.update();
        }
    }

}
