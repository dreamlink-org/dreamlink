package dreamlink.graphics.program.strategy.model;

import dreamlink.graphics.program.uniform.ShaderMatrix4fUniform;

public interface IModelStrategyProvider {

    public ShaderMatrix4fUniform getModelMatrixUniform();
    
}
