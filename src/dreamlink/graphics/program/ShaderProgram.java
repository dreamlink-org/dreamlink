package dreamlink.graphics.program;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.shader.ProgramShaderModule;
import dreamlink.graphics.program.shader.ShaderType;
import dreamlink.graphics.program.uniform.ShaderUniform;
import dreamlink.logger.Logger;

public abstract class ShaderProgram {

    private static ShaderProgram currentlyBoundShaderProgram;

    public static ShaderProgram getCurrentlyBoundShaderProgram() {
        return ShaderProgram.currentlyBoundShaderProgram;
    }

    public static void unbind() {
        GL42.glUseProgram(0);
        ShaderProgram.currentlyBoundShaderProgram = null;
    }

    private ProgramShaderModule vertexShader;
    private ProgramShaderModule fragmentShader;
    public int programID;
    private List<ShaderUniform<?>> uniforms;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShader = new ProgramShaderModule(this, vertexShaderPath, ShaderType.vertex);
        this.fragmentShader = new ProgramShaderModule(this, fragmentShaderPath, ShaderType.fragment);
        this.uniforms = new ArrayList<>();
    }

    public void addUniform(ShaderUniform<?> uniform) {
        this.uniforms.add(uniform);
    }

    public void setup() {
        Logger.instance.debug(String.format("Setting up shader program: %s", this));
        this.programID = GL42.glCreateProgram();

        this.vertexShader.loadShaderCode();
        this.vertexShader.setup();

        this.fragmentShader.loadShaderCode();
        this.fragmentShader.setup();

        GL42.glLinkProgram(this.programID);
        GL42.glValidateProgram(this.programID);

        for(var uniform : this.uniforms) {
            uniform.setup();
        }
    }

    public void bind() {
        GL42.glUseProgram(this.programID);
        ShaderProgram.currentlyBoundShaderProgram = this;
    }

    @Override
    public String toString() {
        var hash = Integer.toHexString(this.hashCode());
        return String.format("ShaderProgram(%s)", hash);
    }
    
}
