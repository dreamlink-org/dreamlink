package dreamlink.player.velocity;

import dreamlink.player.IPlayerDirectory;
import dreamlink.player.velocity.strategy.FloatVelocityStrategy;
import dreamlink.player.velocity.strategy.PlatformerVelocityStrategy;

public class PlayerVelocityInputController {

    private static float platformerMaxWalkHorizontalSpeed = 6f;
    private static float platformerMaxSprintHorizontalSpeed = 9f;
    private static float platformerMaxCrouchHorizontalSpeed = 3.5f;
    private static float platformerGroundAcceleration = 3f;
    private static float platformerAirAcceleration = 2f;
    private static float platformGroundFriction = 1f;
    private static float platformAirFriction = 0.1f;
    private static float platformerVerticalJumpSpeed = 10f;
    private static float platformerGravity = 0.35f;
    private static float platformerMaxFallSpeed = 8f;

    private static float swimMaxSpeed = 3.5f;
    private static float swimMaxCrouchSpeed = 2f;
    private static float swimAcceleration = 1f;
    private static float swimFriction = 0.3f;

    private static float editMaxSpeed = 5f;
    private static float editMaxCrouchSpeed = 5f;
    private static float editMaxSprintSpeed = 14f;
    private static float editAcceleration = 10f;
    private static float editFriction = 1f;


    private static PlatformerVelocityStrategy platformerStrategy = new PlatformerVelocityStrategy(
        PlayerVelocityInputController.platformerMaxWalkHorizontalSpeed,
        PlayerVelocityInputController.platformerMaxSprintHorizontalSpeed,
        PlayerVelocityInputController.platformerMaxCrouchHorizontalSpeed,
        PlayerVelocityInputController.platformerGroundAcceleration,
        PlayerVelocityInputController.platformerAirAcceleration,
        PlayerVelocityInputController.platformGroundFriction,
        PlayerVelocityInputController.platformAirFriction,
        PlayerVelocityInputController.platformerMaxFallSpeed,
        PlayerVelocityInputController.platformerVerticalJumpSpeed,
        PlayerVelocityInputController.platformerGravity
    );

    private static FloatVelocityStrategy swimStrategy = new FloatVelocityStrategy(
        PlayerVelocityInputController.swimMaxSpeed,
        PlayerVelocityInputController.swimMaxCrouchSpeed,
        PlayerVelocityInputController.swimMaxSpeed,
        PlayerVelocityInputController.swimAcceleration,
        PlayerVelocityInputController.swimFriction
    );

    private static FloatVelocityStrategy editStrategy = new FloatVelocityStrategy(
        PlayerVelocityInputController.editMaxSpeed,
        PlayerVelocityInputController.editMaxCrouchSpeed,
        PlayerVelocityInputController.editMaxSprintSpeed,
        PlayerVelocityInputController.editAcceleration,
        PlayerVelocityInputController.editFriction
    );

    private IPlayerDirectory directory;

    public PlayerVelocityInputController(IPlayerDirectory directory) {
        this.directory = directory;
    }

    public void updateVelocity() {
        if(this.directory.isNoClip()) {
            PlayerVelocityInputController.editStrategy.updateVelocity(this.directory);
            return;
        }

        var state = this.directory.getPlayerState();
        if(state.isSwimming) {
            PlayerVelocityInputController.swimStrategy.updateVelocity(this.directory);
        } else {
            PlayerVelocityInputController.platformerStrategy.updateVelocity(this.directory);
        }
    }
    
}
