package dreamlink.graphics.program.strategy.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dreamlink.Config;

public class CameraStrategy {

    private static float nearPlane = 0.05f;
    private static float farPlane = 10_000f;
    private static float fieldOfView = (float)Math.toRadians(75f); 
    private static Matrix4f matrixBuffer = new Matrix4f();

    private ICameraStrategyProvider provider;

    public CameraStrategy(ICameraStrategyProvider provider) {
        this.provider = provider;
    }

    private void setViewRotationMatrix(Vector3f rotation) {
        // If the CAMERA is facing in the positive Z (FRONT) direction, then we will be seeing all of the
        // BACK faces. Thus, we need to rotate our camera by 180 degrees.
        CameraStrategy.matrixBuffer
            .identity()
            .rotateY((float)Math.PI)
            .rotateX(-rotation.x)
            .rotateY(-rotation.y);

        this.provider
            .getViewRotationMatrixUniform()
            .setValue(CameraStrategy.matrixBuffer);
    }

    private void setViewTranslationMatrix(Vector3f position) {
        CameraStrategy.matrixBuffer
            .identity()
            .translate(-position.x, -position.y, -position.z);

        this.provider
            .getViewTranslationMatrixUniform()
            .setValue(CameraStrategy.matrixBuffer);
    }

    private void setProjectionMatrix() {
        var aspectRatio = (float)Config.instance.resolution.x / Config.instance.resolution.y;
        CameraStrategy.matrixBuffer
            .identity()
            .perspective(
                CameraStrategy.fieldOfView, 
                aspectRatio, 
                CameraStrategy.nearPlane, 
                CameraStrategy.farPlane
            );

        this.provider
            .getViewProjectionMatrixUniform()
            .setValue(CameraStrategy.matrixBuffer);
    }

    public void setCamera(Vector3f position, Vector3f rotation) {
        this.setViewRotationMatrix(rotation);
        this.setViewTranslationMatrix(position);
        this.setProjectionMatrix();
    }
    
}
