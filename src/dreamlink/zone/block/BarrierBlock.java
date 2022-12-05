package dreamlink.zone.block;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.block.meshwriter.IPrimitiveBlock;
import dreamlink.zone.block.meshwriter.PrimitiveBlockMeshWriterStrategy;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public class BarrierBlock implements IPrimitiveBlock {

    public static short blockID = 0x01;
    private static String blockName = "Barrier Block";

    private IZoneDirectory directory;

    public BarrierBlock(IZoneDirectory directory) {
        this.directory = directory;
    }

    @Override
    public int getBlockID() {
        return BarrierBlock.blockID;
    }

    @Override
    public String getBlockName() {
        return BarrierBlock.blockName;
    }

    @Override
    public int getLocalLight() {
        return 0;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean isAffectedByLight() {
        return false;
    }

    @Override
    public boolean canLightEnter(CubeFace cubeFace) {
        return false;
    }

    @Override
    public boolean canLightExit(CubeFace cubeFace) {
        return false;
    }

    @Override
    public TextureSample getTextureSample(Vector3i position, CubeFace face) {
        return EntityTextureSample.barrier;
    }

    @Override
    public void writeToChunkMesh(
        TerrainChunk chunk,
        Vector3i position, 
        TerrainBlockData blockData
    ) {
        PrimitiveBlockMeshWriterStrategy.instance.writeToChunkMesh(
            this.directory,
            chunk, 
            position,
            this,
            Orientation.getOrientation(blockData.orientationID)
        );
    }

    @Override
    public AABBf getCollider(
        AABBf collider, 
        Vector3i blockPosition
    ) {
        return collider
            .setMin(blockPosition.x, blockPosition.y, blockPosition.z)
            .setMax(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z + 1);
    }
}
