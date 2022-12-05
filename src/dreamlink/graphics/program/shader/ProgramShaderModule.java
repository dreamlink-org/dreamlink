package dreamlink.graphics.program.shader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.lwjgl.opengl.GL42;

import dreamlink.graphics.program.ShaderProgram;

public class ProgramShaderModule {

    private int shaderID;
    private ShaderType shaderType;
    private ShaderProgram shaderProgram;
    private String shaderPath;
    private String shaderCode;

    public ProgramShaderModule(ShaderProgram shaderProgram, String shaderPath, ShaderType shaderType) {
        this.shaderProgram = shaderProgram;
        this.shaderPath = shaderPath;
        this.shaderType = shaderType;
    }

    public void loadShaderCode() {
        try(
            var stream = this.getClass().getClassLoader().getResourceAsStream(this.shaderPath);
        ) {
            this.shaderCode = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setup() {
        this.shaderID = GL42.glCreateShader(this.shaderType.shaderTypeID);
        GL42.glShaderSource(this.shaderID, this.shaderCode);
        GL42.glCompileShader(this.shaderID);
        if(GL42.glGetShaderi(this.shaderID, GL42.GL_COMPILE_STATUS) == 0) {
            System.err.println(GL42.glGetShaderInfoLog(this.shaderID));
            throw new RuntimeException("Shader compilation failed");
        }
        GL42.glAttachShader(this.shaderProgram.programID, this.shaderID);
    }

    public void delete() {
        GL42.glDetachShader(this.shaderProgram.programID, this.shaderID);
        GL42.glDeleteShader(this.shaderID);
    }

    
}
