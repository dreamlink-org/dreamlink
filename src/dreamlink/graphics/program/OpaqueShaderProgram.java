package dreamlink.graphics.program;

import dreamlink.graphics.program.uniform.ShaderIntegerUniform;

public class OpaqueShaderProgram extends TerrainShaderProgram {

    private static String vertexPath = "glsl/opaque/Vertex.glsl";
    private static String fragmentPath = "glsl/opaque/Fragment.glsl";

    public static int entityTextureUnitID = 0;
    public static int zoneTextureUnitID = 1;
    public static int portalTextureUnitID = 2;

    private static String uniformSamplerEntity = "sampler_entity";
    private static String uniformSamplerZone = "sampler_zone";
    private static String uniformSamplerPortal = "sampler_portal";

    public static OpaqueShaderProgram instance = new OpaqueShaderProgram();

    private ShaderIntegerUniform samplerEntity;
    private ShaderIntegerUniform samplerZone;
    private ShaderIntegerUniform samplerPortal;

    public OpaqueShaderProgram() {
        super(OpaqueShaderProgram.vertexPath, OpaqueShaderProgram.fragmentPath);
        this.samplerEntity = new ShaderIntegerUniform(this, OpaqueShaderProgram.uniformSamplerEntity);
        this.samplerZone = new ShaderIntegerUniform(this, OpaqueShaderProgram.uniformSamplerZone);
        this.samplerPortal = new ShaderIntegerUniform(this, OpaqueShaderProgram.uniformSamplerPortal);
    }

    @Override
    public void setup() {
        super.setup();

        var previouslyBoundShaderProgram = ShaderProgram.getCurrentlyBoundShaderProgram();
        try {
            this.bind();
            this.samplerEntity.setValue(OpaqueShaderProgram.entityTextureUnitID);
            this.samplerZone.setValue(OpaqueShaderProgram.zoneTextureUnitID);
            this.samplerPortal.setValue(OpaqueShaderProgram.portalTextureUnitID);
        } finally {
            if(previouslyBoundShaderProgram != null) {
                previouslyBoundShaderProgram.bind();
            } else {
                ShaderProgram.unbind();
            }
        }
    }
}
