package dreamlink.graphics.program.uniform;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public class ShaderVector3fUniform extends ShaderUniform<Vector3f> {

    public ShaderVector3fUniform(ShaderProgram program, String uniformName) {
        super(program, uniformName);
    }

    public void setValue(Vector3f value) {
        GL42.glUniform3f(
            this.getUniformID(),
            value.x,
            value.y,
            value.z
        );
    }
    
}
