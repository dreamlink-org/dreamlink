package dreamlink.player;

import dreamlink.Config;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.FloatMaths;

import org.joml.Vector2f;

import dreamlink.window.Window;

public class PlayerRotationInputController {

    private static float pitchLimit = (float)Math.toRadians(88f);
    private static float fullRotation = (float)Math.PI * 2f;

    private IPlayerDirectory directory;

    PlayerRotationInputController(IPlayerDirectory directory) {
        this.directory = directory;
    }

    public void update() {
        var state = this.directory.getPlayerState();
        var mouseSensitivity = Config.instance.mouseSensitivity;
        var adjustedMouseDelta = new Vector2f(Window.instance.getDeltaMousePosition())
            .set(Window.instance.getDeltaMousePosition())
            .mul(mouseSensitivity * Simulation.updateFactor)
            // in 2D screen space, left and up are negative, right and down are positive.
            // in 3D world space, right and down are negative, left and up are positive. 
            // Thus both axes need to be scaled by -1.
            .mul(-1f);

        // For _pitch_, using the right hand rule, stick your thumb left (+ve X) and notice your fingers curl down.
        // In our coordinate system, down is negative, so we SUBTRACT the mouse delta.
        state.rotation.x -= adjustedMouseDelta.y;
        state.rotation.x = FloatMaths.clamp(
            state.rotation.x, 
            -PlayerRotationInputController.pitchLimit, 
            PlayerRotationInputController.pitchLimit
        );

        // For _yaw_, using the right hand rule, stick your thumb up (+ve Y) and notice your fingers curl left.
        // In our coordinate system, left is positive, so we ADD the mouse delta.
        state.rotation.y += adjustedMouseDelta.x;

        var yaw = state.rotation.y;
        state.rotation.y = FloatMaths.floatMod(
            yaw, 
            PlayerRotationInputController.fullRotation
        );


    }
}
