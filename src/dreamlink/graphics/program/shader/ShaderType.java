package dreamlink.graphics.program.shader;

import org.lwjgl.opengl.GL42;

public class ShaderType {

    public static ShaderType vertex = new ShaderType(GL42.GL_VERTEX_SHADER);
    public static ShaderType fragment = new ShaderType(GL42.GL_FRAGMENT_SHADER);

    public int shaderTypeID;

    public ShaderType(int shaderTypeID) {
        this.shaderTypeID = shaderTypeID;
    }
    
}
