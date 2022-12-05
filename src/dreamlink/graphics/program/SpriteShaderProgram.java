package dreamlink.graphics.program;

import dreamlink.graphics.program.uniform.ShaderIntegerUniform;

public class SpriteShaderProgram extends ShaderProgram {

    public static int entityTextureUnitID = 0;
    public static int zoneTextureUnitID = 1;
    public static int overlayTextureUnitID = 2;

    private static String vertexPath = "glsl/sprite/Vertex.glsl";
    private static String fragmentPath = "glsl/sprite/Fragment.glsl";

    private static String uniformAnimationFrame = "animation_frame";
    private static String uniformSamplerEntity = "sampler_entity";
    private static String uniformSamplerZone = "sampler_zone";
    private static String uniformSamplerOverlay = "sampler_overlay";

    public static SpriteShaderProgram instance = new SpriteShaderProgram();

    private ShaderIntegerUniform samplerEntity;
    private ShaderIntegerUniform samplerZone;
    private ShaderIntegerUniform samplerOverlay;
    private ShaderIntegerUniform animationFrame;

    public SpriteShaderProgram() {
        super(SpriteShaderProgram.vertexPath, SpriteShaderProgram.fragmentPath);

        this.samplerEntity = new ShaderIntegerUniform(this, SpriteShaderProgram.uniformSamplerEntity);
        this.samplerZone = new ShaderIntegerUniform(this, SpriteShaderProgram.uniformSamplerZone);
        this.samplerOverlay = new ShaderIntegerUniform(this, SpriteShaderProgram.uniformSamplerOverlay);
        this.animationFrame = new ShaderIntegerUniform(this, SpriteShaderProgram.uniformAnimationFrame);
    }

    public void setAnimationFrame(int frame) {
        this.animationFrame.setValue(frame);
    }

    @Override
    public void setup() {
        super.setup();

        var previouslyBoundShaderProgram = ShaderProgram.getCurrentlyBoundShaderProgram();
        try {
            this.bind();
            this.samplerEntity.setValue(SpriteShaderProgram.entityTextureUnitID);
            this.samplerZone.setValue(SpriteShaderProgram.zoneTextureUnitID);
            this.samplerOverlay.setValue(SpriteShaderProgram.overlayTextureUnitID);
        } finally {
            if(previouslyBoundShaderProgram != null) {
                previouslyBoundShaderProgram.bind();
            } else {
                ShaderProgram.unbind();
            }
        }
    }
    
}
