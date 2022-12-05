package dreamlink.graphics.program.uniform;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

import dreamlink.graphics.program.ShaderProgram;

public class ShaderMatrix4fUniform extends ShaderUniform<Matrix4f> {

    private static FloatBuffer matrixFloatBuffer = MemoryUtil.memAllocFloat(16);

    public ShaderMatrix4fUniform(ShaderProgram program, String uniformName) {
        super(program, uniformName);
    }

    public void setValue(Matrix4f value) {
        ShaderMatrix4fUniform.matrixFloatBuffer.clear();
        value.get(ShaderMatrix4fUniform.matrixFloatBuffer);
        GL42.glUniformMatrix4fv(
            this.getUniformID(),
            false,
            ShaderMatrix4fUniform.matrixFloatBuffer
        );
    }
}
