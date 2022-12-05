package dreamlink.graphics.program.uniform;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public abstract class ShaderUniform<T> {

    private int uniformID;
    private String uniformName;
    private ShaderProgram program;

    public ShaderUniform(ShaderProgram program, String uniformName) {
        this.program = program;
        this.uniformName = uniformName;
        program.addUniform(this);
    }

    public void setup() {
        this.uniformID = GL42.glGetUniformLocation(
            this.program.programID,
            this.uniformName
        );
    }

    protected int getUniformID() {
        return this.uniformID;
    }

    public abstract void setValue(T value);

}
