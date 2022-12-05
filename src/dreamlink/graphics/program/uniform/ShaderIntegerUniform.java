package dreamlink.graphics.program.uniform;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public class ShaderIntegerUniform extends ShaderUniform<Integer> {

    public ShaderIntegerUniform(ShaderProgram program, String uniformName) {
        super(program, uniformName);
    }

    @Override
    public void setValue(Integer value) {
        GL42.glUniform1i(
            this.getUniformID(),
            value.intValue()
        );
    }
    
}
