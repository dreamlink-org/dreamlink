package dreamlink.player.velocity.strategy;

import dreamlink.window.Window;
import dreamlink.window.button.Button;

import org.joml.Vector3f;

import dreamlink.player.IPlayerDirectory;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3fMaths;

public class PlatformerVelocityStrategy {

    private static float fuzzEpsilon = 0.001f;
    private static float waterJumpGracePeriod = 100;

    private float maxWalkHorizontalSpeed;
    private float maxSprintHorizontalSpeed;
    private float maxCrouchHorizontalSpeed;
    private float groundAcceleration;
    private float airAcceleration;
    private float groundFriction;
    private float airFriction;
    private float maxFallSpeed;
    private float jumpSpeed;
    private float gravity;

    public PlatformerVelocityStrategy(
        float maxWalkHorizontalSpeed,
        float maxSprintHorizontalSpeed,
        float maxCrouchHorizontalSpeed,
        float groundAcceleration,
        float airAcceleration,
        float groundFriction,
        float airFriction,
        float maxFallSpeed,
        float jumpSpeed,
        float gravity
    ) {
        this.maxWalkHorizontalSpeed = maxWalkHorizontalSpeed * Simulation.updateFactor;
        this.maxSprintHorizontalSpeed = maxSprintHorizontalSpeed * Simulation.updateFactor;
        this.maxCrouchHorizontalSpeed = maxCrouchHorizontalSpeed * Simulation.updateFactor;
        this.groundAcceleration = groundAcceleration * Simulation.updateFactor;
        this.airAcceleration = airAcceleration * Simulation.updateFactor;
        this.groundFriction = groundFriction * Simulation.updateFactor;
        this.airFriction = airFriction * Simulation.updateFactor;
        this.maxFallSpeed = maxFallSpeed * Simulation.updateFactor;
        this.jumpSpeed = jumpSpeed * Simulation.updateFactor;
        this.gravity = gravity * Simulation.updateFactor;
    }

    private void updateHorizontalVelocity(IPlayerDirectory directory) {
        var state = directory.getPlayerState();
        var velocity = state.velocity;
        var horizontalVelocity = new Vector3f(velocity.x, 0, velocity.z);

        // Get the current speed of the player.
        var speed = horizontalVelocity.length();

        // Apply friction only when on the ground.
        var isOnGround = state.isInContact(CubeFace.bottom);
        var friction = isOnGround
            ? this.groundFriction
            : this.airFriction;

        var reducedSpeed = Math.max(0, speed - friction);
        var fuzzedSpeed = speed + PlatformerVelocityStrategy.fuzzEpsilon;
        horizontalVelocity.mul(reducedSpeed / fuzzedSpeed);

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
        }

        wishDirection.rotateY(state.rotation.y);
        Vector3fMaths.safeNormalize(wishDirection, PlatformerVelocityStrategy.fuzzEpsilon);
        
        float maxSpeed;
        if(state.isCrouching) {
            maxSpeed = this.maxCrouchHorizontalSpeed;
        } else if(Window.instance.isButtonDown(Button.keyLeftShift)) {
            maxSpeed = this.maxSprintHorizontalSpeed;
        } else {
            maxSpeed = this.maxWalkHorizontalSpeed;
        }
        
        // Calculate the horizontal velocity.
        var acceleration = isOnGround 
            ? this.groundAcceleration 
            : this.airAcceleration;
        if(acceleration + speed > maxSpeed) {
            acceleration = Math.max(maxSpeed - speed, 0f);
        }

        wishDirection.mul(acceleration);
        horizontalVelocity.add(wishDirection);

        velocity.x = horizontalVelocity.x;
        velocity.z = horizontalVelocity.z;
    }

    private void updateJump(IPlayerDirectory directory) {
        if(!Window.instance.isButtonDown(Button.keySpace)) {
            return;
        }

        var state = directory.getPlayerState();
        var isOnGround = state.isInContact(CubeFace.bottom);

        if(isOnGround) {
            state.velocity.y += this.jumpSpeed;
            return;
        }

        var currentTime = System.currentTimeMillis();
        if(currentTime - state.lastSwimTime < PlatformerVelocityStrategy.waterJumpGracePeriod) {
            state.velocity.y = this.jumpSpeed;
            return;
        }
    }

    public void updateVelocity(IPlayerDirectory directory) {
        this.updateJump(directory);
        this.updateHorizontalVelocity(directory);

        var state = directory.getPlayerState();
        state.velocity.y = Math.max(
            - this.maxFallSpeed,
            state.velocity.y - this.gravity
        );
        Vector3fMaths.fuzz(state.velocity, PlatformerVelocityStrategy.fuzzEpsilon);
    }
    
}
