package dreamlink.graphics.program;

import dreamlink.graphics.program.uniform.ShaderIntegerUniform;

public class CompositeShaderProgram extends ShaderProgram {

    public static int opaqueTextureUnitID = 0;
    public static int transparentAccumulatorTextureUnitID = 1;
    public static int transparentRevealTextureUnitID = 2;

    private static String vertexPath = "glsl/composite/Vertex.glsl";
    private static String fragmentPath = "glsl/composite/Fragment.glsl";

    private static String uniformSamplerOpaque = "sampler_opaque";
    private static String uniformSamplerTransparentAccumulator = "sampler_transparent_accumulator";
    private static String uniformSamplerTransparentReveal = "sampler_transparent_reveal";

    public static CompositeShaderProgram instance = new CompositeShaderProgram();

    private ShaderIntegerUniform samplerOpaque;
    private ShaderIntegerUniform samplerTransparentAccumulator;
    private ShaderIntegerUniform samplerTransparentReveal;

    public CompositeShaderProgram() {
        super(CompositeShaderProgram.vertexPath, CompositeShaderProgram.fragmentPath);
        this.samplerOpaque = new ShaderIntegerUniform(this, CompositeShaderProgram.uniformSamplerOpaque);
        this.samplerTransparentAccumulator = new ShaderIntegerUniform(this, CompositeShaderProgram.uniformSamplerTransparentAccumulator);
        this.samplerTransparentReveal = new ShaderIntegerUniform(this, CompositeShaderProgram.uniformSamplerTransparentReveal);
    }

    @Override
    public void setup() {
        super.setup();

        var previouslyBoundShaderProgram = ShaderProgram.getCurrentlyBoundShaderProgram();
        try {
            this.bind();
            this.samplerOpaque.setValue(CompositeShaderProgram.opaqueTextureUnitID);
            this.samplerTransparentAccumulator.setValue(CompositeShaderProgram.transparentAccumulatorTextureUnitID);
            this.samplerTransparentReveal.setValue(CompositeShaderProgram.transparentRevealTextureUnitID);
        } finally {
            if(previouslyBoundShaderProgram != null) {
                previouslyBoundShaderProgram.bind();
            } else {
                ShaderProgram.unbind();
            }
        }
    }
    
}
