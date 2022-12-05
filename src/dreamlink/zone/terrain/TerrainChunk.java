package dreamlink.zone.terrain;

import org.joml.Vector3i;

import dreamlink.graphics.mesh.TerrainMesh;
import dreamlink.utility.maths.Vector3iMaths;

public class TerrainChunk {

    public static Vector3i chunkDimensions = new Vector3i(16);
    public static int blockCount = Vector3iMaths.getVolume(TerrainChunk.chunkDimensions);

    private short[] data;
    private boolean isSetup;
    public TerrainMesh opaqueMesh;
    public TerrainMesh transparentMesh;
    public Vector3i position;

    public TerrainChunk(Vector3i position) {
        this.data = new short[TerrainChunk.blockCount];
        this.opaqueMesh = new TerrainMesh();
        this.transparentMesh = new TerrainMesh();
        this.position = new Vector3i(position);
    }

    public short getData(int index) {
        return this.data[index];
    }

    public boolean isSetup() {
        return this.isSetup;
    }

    public void setData(int index, short value) {
        this.data[index] = value;
    }

    public void setup() {
        this.opaqueMesh.setup();
        this.transparentMesh.setup();
        this.isSetup = true;
    }

    public void destroy() {
        if(this.isSetup) {
            this.opaqueMesh.destroy();
            this.transparentMesh.destroy(); 
        }
    }

}
