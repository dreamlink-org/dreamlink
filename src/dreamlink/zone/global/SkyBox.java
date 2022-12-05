package dreamlink.zone.global;

import org.joml.Vector3f;

import dreamlink.graphics.mesh.TerrainMesh;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;

public class SkyBox {

    private static float skyBoxLength = 5_000f;

    private TerrainMesh mesh;
    public boolean isAffectedByLight;
    private TextureSample[] textureSamples;

    public SkyBox() {
        this.textureSamples = new TextureSample[6];
        this.mesh = new TerrainMesh();
    }

    public void setup() {
        this.mesh.setup();
    }

    public void setTextureEntryName(CubeFace cubeFace, TextureSample textureSample) {
        this.textureSamples[cubeFace.cubeFaceID] = textureSample;
    }

    public void buildMesh() {
        var dimensions = new Vector3f(SkyBox.skyBoxLength);
        var position = new Vector3f(SkyBox.skyBoxLength).mul(-0.5f);
        this.mesh.clear();

        for(var cubeFace : CubeFace.getCubeFaces()) {
            var textureSample = this.textureSamples[cubeFace.cubeFaceID];
            if(textureSample == null) {
                continue;
            }

            this.mesh.addQuad(
                position,
                dimensions,
                cubeFace,
                Orientation.front,
                textureSample,
                0x0,
                0x0,
                false,
                this.isAffectedByLight
            );
        }
        this.mesh.buffer();
    }

    public void render() {
        this.mesh.render();
    }

    public void destroy() {
        this.mesh.destroy();
    }
    
}
