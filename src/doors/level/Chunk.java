package doors.level;

import java.io.FileInputStream;
import java.io.IOException;

import doors.core.graphics.mesh.Mesh;
import doors.core.utility.vector.Vector3in;

public class Chunk {

    public static Vector3in DIMENSIONS = new Vector3in(16, 16, 16);

    public final Mesh mesh;
    public final Vector3in position;

    public boolean isDirty;

    private int[] blockData;
    private String path;

    public Chunk(Vector3in position, String path) {
        this.mesh = new Mesh();
        this.position = position;
        this.path = path;
        this.blockData = new int[DIMENSIONS.getIntVolume()];
    }

    public void setup() {
        this.mesh.setup();
        try(var fileInputStream = new FileInputStream(this.path)) {
            for(var ix = 0; ix < this.blockData.length; ix += 1) {
                var blockID = 0;
                blockID += fileInputStream.read();
                blockID += fileInputStream.read() * 0x100;
                this.blockData[ix] = blockID;
            }

        } catch (IOException e) {
            var msg = String.format("Unable to load chunk: %s", this.path);
            throw new RuntimeException(msg);
        }
    }

    public boolean setBlockID(Vector3in position, int blockID) {
        var blockIndex = position.serialize(DIMENSIONS);
        var oldBlockID = this.blockData[blockIndex];
        var madeDirty = oldBlockID != blockID && !this.isDirty;
        this.blockData[blockIndex] = blockID;
        this.isDirty |= madeDirty;
        return madeDirty;

    }

    public int getBlockID(Vector3in position) {
        var blockIndex = position.serialize(DIMENSIONS);
        return this.blockData[blockIndex];
    }
}