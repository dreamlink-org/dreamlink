package dreamlink.graphics.program.uniform;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public class ShaderFloatUniform extends ShaderUniform<Float> {

    public ShaderFloatUniform(ShaderProgram program, String uniformName) {
        super(program, uniformName);
    }

    @Override
    public void setValue(Float value) {
        GL42.glUniform1f(
            this.getUniformID(),
            value.floatValue()
        );
    }
    
}
