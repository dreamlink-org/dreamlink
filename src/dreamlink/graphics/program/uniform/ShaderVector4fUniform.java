package dreamlink.graphics.program.uniform;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public class ShaderVector4fUniform extends ShaderUniform<Vector4f> {

    public ShaderVector4fUniform(ShaderProgram program, String uniformName) {
        super(program, uniformName);
    }

    public void setValue(Vector4f value) {
        GL42.glUniform4f(
            this.getUniformID(),
            value.x,
            value.y,
            value.z,
            value.w
        );
    }
    
}
