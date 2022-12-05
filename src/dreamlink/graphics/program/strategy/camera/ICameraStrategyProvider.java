package dreamlink.graphics.program.strategy.camera;

import dreamlink.graphics.program.uniform.ShaderMatrix4fUniform;

public interface ICameraStrategyProvider {

    public ShaderMatrix4fUniform getViewProjectionMatrixUniform();

    public ShaderMatrix4fUniform getViewRotationMatrixUniform();

    public ShaderMatrix4fUniform getViewTranslationMatrixUniform();
    
}
