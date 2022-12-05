package dreamlink.graphics.program;

import dreamlink.graphics.program.uniform.ShaderIntegerUniform;

public class TransparentShaderProgram extends TerrainShaderProgram {

    private static String vertexPath = "glsl/transparent/Vertex.glsl";
    private static String fragmentPath = "glsl/transparent/Fragment.glsl";

    public static int entityTextureUnitID = 0;
    public static int zoneTextureUnitID = 1;

    private static String uniformSamplerEntity = "sampler_entity";
    private static String uniformSamplerZone = "sampler_zone";

    public static TransparentShaderProgram instance = new TransparentShaderProgram();

    private ShaderIntegerUniform samplerEntity;
    private ShaderIntegerUniform samplerZone;

    public TransparentShaderProgram() {
        super(TransparentShaderProgram.vertexPath, TransparentShaderProgram.fragmentPath);
        this.samplerEntity = new ShaderIntegerUniform(this, TransparentShaderProgram.uniformSamplerEntity);
        this.samplerZone = new ShaderIntegerUniform(this, TransparentShaderProgram.uniformSamplerZone);
    }

    @Override
    public void setup() {
        super.setup();

        var previouslyBoundShaderProgram = ShaderProgram.getCurrentlyBoundShaderProgram();
        try {
            this.bind();
            this.samplerEntity.setValue(TransparentShaderProgram.entityTextureUnitID);
            this.samplerZone.setValue(TransparentShaderProgram.zoneTextureUnitID);
        } finally {
            if(previouslyBoundShaderProgram != null) {
                previouslyBoundShaderProgram.bind();
            } else {
                ShaderProgram.unbind();
            }
        }
    }
}
