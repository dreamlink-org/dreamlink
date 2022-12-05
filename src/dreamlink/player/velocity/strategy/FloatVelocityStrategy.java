package dreamlink.player.velocity.strategy;

import org.joml.Vector3f;

import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.window.Window;
import dreamlink.window.button.Button;

public class FloatVelocityStrategy {

    private static float fuzzEpsilon = 0.001f;

    private float maxSpeed;
    private float maxCreepSpeed;
    private float maxSprintSpeed;
    private float acceleration;
    private float friction;

    public FloatVelocityStrategy(
        float maxSpeed,
        float maxCrouchSpeed,
        float maxSprintSpeed,
        float acceleration,
        float friction
    ) {
        this.maxSpeed = maxSpeed * Simulation.updateFactor;
        this.maxCreepSpeed = maxCrouchSpeed * Simulation.updateFactor;
        this.maxSprintSpeed = maxSprintSpeed * Simulation.updateFactor;
        this.acceleration = acceleration * Simulation.updateFactor;
        this.friction = friction * Simulation.updateFactor;
    }

    public void updateVelocity(IPlayerDirectory directory) {
        var state = directory.getPlayerState();

        // Get the current speed of the player.
        var speed = state.velocity.length();

        var reducedSpeed = Math.max(speed - this.friction, 0f);
        var fuzzedSpeed = Math.max(speed, FloatVelocityStrategy.fuzzEpsilon);
        state.velocity.mul(reducedSpeed / fuzzedSpeed);

        // Calculate the direction the player wants to move in.
        var wishDirection = new Vector3f();
        if(!Window.instance.isButtonDown(Button.keyLeftAlt)) {
            if(Window.instance.isButtonDown(Button.keyW)) {
                Vector3fMaths.add(wishDirection, CubeFace.front.normal);
            }
            if(Window.instance.isButtonDown(Button.keyS)) {
                Vector3fMaths.add(wishDirection, CubeFace.back.normal);
            }
            if(Window.instance.isButtonDown(Button.keyD)) {
                Vector3fMaths.add(wishDirection, CubeFace.right.normal);
            }
            if(Window.instance.isButtonDown(Button.keyA)) {
                Vector3fMaths.add(wishDirection, CubeFace.left.normal);
            }
            if(Window.instance.isButtonDown(Button.keySpace)) {
                Vector3fMaths.add(wishDirection, CubeFace.top.normal);
            }
        }

        wishDirection.rotateX(state.rotation.x);
        wishDirection.rotateY(state.rotation.y);

        Vector3fMaths.safeNormalize(wishDirection, FloatVelocityStrategy.fuzzEpsilon);

        float maxSpeed;
        if(state.isCrouching) {
            maxSpeed = this.maxCreepSpeed;
        } else if(Window.instance.isButtonDown(Button.keyLeftShift)) {
            maxSpeed = this.maxSprintSpeed;
        } else {
            maxSpeed = this.maxSpeed;
        }

        var acceleration = this.acceleration;
        if(acceleration + speed > maxSpeed) {
            acceleration = Math.max(maxSpeed - speed, 0f);
        }

        wishDirection.mul(acceleration);
        state.velocity.add(wishDirection);
        Vector3fMaths.fuzz(state.velocity, FloatVelocityStrategy.fuzzEpsilon);
    }
    
}
