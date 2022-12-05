package dreamlink.graphics.program;

import org.joml.Vector3f;

import dreamlink.graphics.program.strategy.camera.CameraStrategy;
import dreamlink.graphics.program.strategy.camera.ICameraStrategyProvider;
import dreamlink.graphics.program.strategy.clip.ClipStrategy;
import dreamlink.graphics.program.strategy.clip.IClipStrategyProvider;
import dreamlink.graphics.program.strategy.model.IModelStrategyProvider;
import dreamlink.graphics.program.strategy.model.ModelStrategy;
import dreamlink.graphics.program.uniform.ShaderFloatUniform;
import dreamlink.graphics.program.uniform.ShaderIntegerUniform;
import dreamlink.graphics.program.uniform.ShaderMatrix4fUniform;
import dreamlink.graphics.program.uniform.ShaderVector4fUniform;

public class TerrainShaderProgram extends ShaderProgram {

    private class InternalCameraStrategyProvider implements ICameraStrategyProvider {

        @Override
        public ShaderMatrix4fUniform getViewProjectionMatrixUniform() {
            return TerrainShaderProgram.this.viewProjectionMatrix;
        }

        @Override
        public ShaderMatrix4fUniform getViewRotationMatrixUniform() {
            return TerrainShaderProgram.this.viewRotationMatrix;
        }

        @Override
        public ShaderMatrix4fUniform getViewTranslationMatrixUniform() {
            return TerrainShaderProgram.this.viewTranslationMatrix;
        }

    }

    private class InternalModelStrategyProvider implements IModelStrategyProvider {

        @Override
        public ShaderMatrix4fUniform getModelMatrixUniform() {
            return TerrainShaderProgram.this.modelMatrix;
        }

    }

    private class InternalClipStrategyProvider implements IClipStrategyProvider {

        @Override
        public ShaderVector4fUniform getClipUniform() {
            return TerrainShaderProgram.this.clip;
        }

    }

    private static String uniformViewProjectionMatrix = "view_projection_matrix";
    private static String uniformViewRotationMatrix = "view_rotation_matrix";
    private static String uniformViewTranslationMatrix = "view_translation_matrix";
    private static String uniformModelMatrix = "model_matrix";
    private static String uniformClipPlane = "clip";
    private static String uniformAnimationFrame = "animation_frame";

    private static String uniformBaseLight = "base_light";
    private static String uniformPortalLight = "portal_light";
    private static String uniformIsShowHidden = "is_show_hidden";

    private ShaderMatrix4fUniform viewProjectionMatrix;
    private ShaderMatrix4fUniform viewRotationMatrix;
    private ShaderMatrix4fUniform viewTranslationMatrix;
    private ShaderMatrix4fUniform modelMatrix;
    private ShaderVector4fUniform clip;
    private ShaderFloatUniform baseLight;
    private ShaderFloatUniform portalLight;
    private ShaderIntegerUniform isShowHidden;
    private ShaderIntegerUniform animationFrame;

    private CameraStrategy cameraStrategy;
    private ModelStrategy modelStrategy;
    private ClipStrategy clipStrategy;

    public TerrainShaderProgram(String vertexPath, String fragmentPath) {
        super(vertexPath, fragmentPath);

        this.viewProjectionMatrix = new ShaderMatrix4fUniform(this, TerrainShaderProgram.uniformViewProjectionMatrix);
        this.viewRotationMatrix = new ShaderMatrix4fUniform(this, TerrainShaderProgram.uniformViewRotationMatrix);
        this.viewTranslationMatrix = new ShaderMatrix4fUniform(this, TerrainShaderProgram.uniformViewTranslationMatrix);
        this.modelMatrix = new ShaderMatrix4fUniform(this, TerrainShaderProgram.uniformModelMatrix);
        this.clip = new ShaderVector4fUniform(this, TerrainShaderProgram.uniformClipPlane);
        this.baseLight = new ShaderFloatUniform(this, TerrainShaderProgram.uniformBaseLight);
        this.portalLight = new ShaderFloatUniform(this, TerrainShaderProgram.uniformPortalLight);
        this.isShowHidden = new ShaderIntegerUniform(this, TerrainShaderProgram.uniformIsShowHidden);
        this.animationFrame = new ShaderIntegerUniform(this, TerrainShaderProgram.uniformAnimationFrame);

        this.cameraStrategy = new CameraStrategy(new InternalCameraStrategyProvider());
        this.modelStrategy = new ModelStrategy(new InternalModelStrategyProvider());
        this.clipStrategy = new ClipStrategy(new InternalClipStrategyProvider());
    }

    public void setAnimationFrame(int frame) {
        this.animationFrame.setValue(frame);
    }

    public void setCamera(Vector3f position, Vector3f rotation) {
        this.cameraStrategy.setCamera(position, rotation);
    }

    public void setModel(Vector3f position) {
        this.modelStrategy.setModel(position);
    }

    public void setClip(Vector3f position, Vector3f normal) {
        this.clipStrategy.setClip(position, normal);
    }

    public void setBaseLight(float baseLight) {
        this.baseLight.setValue(baseLight);
    }

    public void setPortalLight(float portalLight) {
        this.portalLight.setValue(portalLight);
    }

    public void isShowHidden(boolean isShowHidden) {
        this.isShowHidden.setValue(isShowHidden ? 1 : 0);
    }
    
}
