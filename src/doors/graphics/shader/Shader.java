package doors.graphics.shader;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

import doors.Config;
import doors.graphics.mesh.TextureSampleMode;
import doors.utility.FileIO;
import doors.utility.vector.Vector3fl;
import doors.utility.vector.IVector2fl;
import doors.utility.vector.IVector3fl;

import java.nio.FloatBuffer;
import java.nio.file.Paths;

public class Shader {
    
    private static int DEFAULT_CUBE_ID = 0;
    private static String SHADER_DIRECTORY = "data/shader";
    private static int FLOATS_PER_MATRIX = 16;
    private static int FLOAT_BUFFER_SIZE = FLOATS_PER_MATRIX * 256;
    private static int NUM_TEXTURE_CHANNELS = 16;

    private static String UNIFORM_VIEW_PROJECTION_MATRIX = "view_projection_matrix";
    private static String UNIFORM_VIEW_ROTATION_MATRIX = "view_rotation_matrix";
    private static String UNIFORM_VIEW_TRANSLATION_MATRIX = "view_translation_matrix";
    private static String UNIFORM_MODEL_MATRIX = "model_matrix";
    private static String UNIFORM_COLOR = "color";
    private static String UNIFORM_TEXTURE_SAMPLE_MODE = "texture_sample_mode";
    private static String UNIFORM_TEXTURE_DIMENSIONS = "texture_dimensions";
    private static String UNIFORM_CUBE_TRANSFORMER_MATRICES = "cube_transformer_matrices";

    private static String UNIFORM_SAMPLER_PREFIX = "sampler_";

    private static float FOV = (float)Math.toRadians(70f);
    private static float NEAR_PLANE = 0.01f;
    private static float FAR_PLANE = 1000f;

    public static Shader SHADER = new Shader();

    private FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(FLOAT_BUFFER_SIZE);
    private Matrix4f workingMatrix = new Matrix4f();

    private int programID;
    private int uniformViewProjectionMatrixID;
    private int uniformViewRotationMatrixID;
    private int uniformViewTranslationMatrixID;
    private int uniformModelMatrixID;
    private int uniformColorID;
    private int uniformCubeTransformerMatricesID;
    private int uniformTextureDimensionsID;
    private int uniformTextureSampleModeID;

    public Shader() {
        this.programID = GL42.glCreateProgram();
        var vertexID = GL42.glCreateShader(GL42.GL_VERTEX_SHADER);
        var vertexShaderPath = Paths.get(SHADER_DIRECTORY, "vertex.glsl").toString();
        this.attachShader(vertexID, FileIO.loadText(vertexShaderPath));

        var fragmentID = GL42.glCreateShader(GL42.GL_FRAGMENT_SHADER);
        var fragmentShaderPath = Paths.get(SHADER_DIRECTORY, "fragment.glsl").toString();
        this.attachShader(fragmentID, FileIO.loadText(fragmentShaderPath));

        GL42.glLinkProgram(this.programID);
        GL42.glValidateProgram(this.programID);
        GL42.glUseProgram(this.programID);

        this.uniformViewProjectionMatrixID = this.createUniformVariable(UNIFORM_VIEW_PROJECTION_MATRIX);
        this.uniformViewRotationMatrixID = this.createUniformVariable(UNIFORM_VIEW_ROTATION_MATRIX);
        this.uniformViewTranslationMatrixID = this.createUniformVariable(UNIFORM_VIEW_TRANSLATION_MATRIX);
        this.uniformModelMatrixID = this.createUniformVariable(UNIFORM_MODEL_MATRIX);
        this.uniformColorID = this.createUniformVariable(UNIFORM_COLOR);
        this.uniformCubeTransformerMatricesID = this.createUniformVariable(UNIFORM_CUBE_TRANSFORMER_MATRICES);
        this.uniformTextureDimensionsID = this.createUniformVariable(UNIFORM_TEXTURE_DIMENSIONS);
        this.uniformTextureSampleModeID = this.createUniformVariable(UNIFORM_TEXTURE_SAMPLE_MODE);

        for(var ix = 0; ix < NUM_TEXTURE_CHANNELS; ix++) {
            var uniformName = UNIFORM_SAMPLER_PREFIX + ix;
            var uniformID = this.createUniformVariable(uniformName);
            this.setUniform(uniformID, ix);
        }

        this.workingMatrix.identity();
        this.setCubeTransformer(DEFAULT_CUBE_ID, Vector3fl.ZERO, Vector3fl.ZERO, Vector3fl.ONE);
    }

    private int createUniformVariable(String uniformName) {
        return GL42.glGetUniformLocation(this.programID, uniformName);
    }

    private void attachShader(int shaderID, String shaderCode) {
        GL42.glShaderSource(shaderID, shaderCode);
        GL42.glCompileShader(shaderID);
        if(GL42.glGetShaderi(shaderID, GL42.GL_COMPILE_STATUS) == 0) {
            System.err.println(GL42.glGetShaderInfoLog(shaderID));
            throw new RuntimeException("Shader compilation failed");
        }
        GL42.glAttachShader(this.programID, shaderID);
    }
    public void setTextureSampleMode(TextureSampleMode mode) {
        this.setUniform(this.uniformTextureSampleModeID, mode.value);
    }

    public void setModelMatrix(IVector3fl position, Vector3fl rotation, Vector3fl scale) {
        this.workingMatrix
            .identity()
            .translate(position.getFloatX(), position.getFloatY(), position.getFloatZ())
            .rotateY(rotation.getFloatY())
            .rotateX(rotation.getFloatX())
            .rotateZ(rotation.getFloatZ())
            .scale(scale.getFloatX(), scale.getFloatY(), scale.getFloatZ());
        this.setUniform(this.uniformModelMatrixID, this.workingMatrix);
    }

    public void setPerspectiveViewMatrices(IVector3fl position, Vector3fl rotation) {
        this.workingMatrix.identity().translate(-position.getFloatX(), -position.getFloatY(), -position.getFloatZ());
        this.setUniform(this.uniformViewTranslationMatrixID, this.workingMatrix);

        // If the CAMERA is facing in the positive Z (FRONT) direction, then we will be seeing all of the
        // BACK faces. Thus, we need to rotate our camera by 180 degrees.
        this.workingMatrix.identity().rotateY((float)Math.PI).rotateX(-rotation.x).rotateY(-rotation.y);
        this.setUniform(this.uniformViewRotationMatrixID, this.workingMatrix);

        var aspectRatio = Config.CONFIG.getResolution().getAspectRatio();
        this.workingMatrix.identity().perspective(FOV, aspectRatio, NEAR_PLANE, FAR_PLANE);
        this.setUniform(this.uniformViewProjectionMatrixID, this.workingMatrix);
    }

    public void setFlatViewMatrices() {
        this.workingMatrix.identity();
        this.setUniform(this.uniformViewTranslationMatrixID, this.workingMatrix);
        this.setUniform(this.uniformViewRotationMatrixID, this.workingMatrix);
        this.setUniform(this.uniformViewProjectionMatrixID, this.workingMatrix);
    }

    public void setCubeTransformer(int cubeID, Vector3fl position, Vector3fl rotation, Vector3fl scale) {
        this.workingMatrix.identity()
            .translate(position.getFloatX(), position.getFloatY(), position.getFloatZ())
            .scale(scale.getFloatX(), scale.getFloatY(), scale.getFloatZ())
            .rotateY(rotation.getFloatY())
            .rotateX(rotation.getFloatX());
        var offsetID = this.uniformCubeTransformerMatricesID + cubeID;
        this.setUniform(offsetID, this.workingMatrix);
    }

    public void setTextureDimensions(int textureUnitID, IVector2fl dimensions) {
        var offsetID = this.uniformTextureDimensionsID + textureUnitID;
        this.setUniform(offsetID, dimensions);
    }

    public void setColor(Vector3fl color) {
        this.setUniform(this.uniformColorID, color);
    }

    private void setUniform(int uniformID, int value) {
        GL42.glUniform1i(uniformID, value);
    }

    private void setUniform(int uniformID, IVector3fl value) {
        GL42.glUniform3f(uniformID, value.getFloatX(), value.getFloatY(), value.getFloatZ());
    }

    private void setUniform(int uniformID, IVector2fl value) {
        GL42.glUniform2f(uniformID, value.getFloatX(), value.getFloatY());
    }

    private void setUniform(int uniformID, Matrix4f value) {
        floatBuffer.clear();
        floatBuffer.limit(FLOATS_PER_MATRIX);
        value.get(floatBuffer);
        GL42.glUniformMatrix4fv(uniformID, false, floatBuffer);
    }

}

