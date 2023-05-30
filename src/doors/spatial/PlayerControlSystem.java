package doors.spatial;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import doors.Game;
import doors.utility.Maths;

public class PlayerControlSystem {

    private static float SPEED = 1f;
    private static float FRICTION = 0.7f;
    private Vector3f velocityBuffer = new Vector3f();

    private void updateVelocity(SpatialComponent component) {
        this.velocityBuffer.zero();

            if(Game.KEYBOARD.isKeyDown(GLFW.GLFW_KEY_S))
                this.velocityBuffer.z += SPEED;

            if(Game.KEYBOARD.isKeyDown(GLFW.GLFW_KEY_W))
                this.velocityBuffer.z -= SPEED;

            if(Game.KEYBOARD.isKeyDown(GLFW.GLFW_KEY_A))
                this.velocityBuffer.x -= SPEED;

            if(Game.KEYBOARD.isKeyDown(GLFW.GLFW_KEY_D))
                this.velocityBuffer.x += SPEED;

        this.velocityBuffer.rotateX(Game.CAMERA.rotation.y);
        this.velocityBuffer.rotateY(Game.CAMERA.rotation.x);
        component.velocity.add(this.velocityBuffer); 
        component.velocity.mul(1 - FRICTION);
        Maths.zeroFuzz(component.velocity);
    }

    private void updatePosition(SpatialComponent component) {
        component.position.add(component.velocity);
        return;
    }

    public void simulate(SpatialComponent component) {
        this.updateVelocity(component);
        this.updatePosition(component);
    }

}
