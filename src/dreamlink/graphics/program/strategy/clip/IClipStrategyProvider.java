package dreamlink.graphics.program.strategy.clip;

import dreamlink.graphics.program.uniform.ShaderVector4fUniform;

public interface IClipStrategyProvider {

    public ShaderVector4fUniform getClipUniform();
    
}
