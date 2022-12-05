package dreamlink.zone.block;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public class AirBlock implements IBlock {

    public static short blockID = 0x00;
    private static String blockName = "Air";

    @Override
    public int getBlockID() {
        return AirBlock.blockID;
    }

    @Override
    public String getBlockName() {
        return AirBlock.blockName;
    }

    @Override
    public int getLocalLight() {
        return 0;
    }

    @Override
    public boolean canLightEnter(CubeFace cubeFace) {
        return true;
    }

    @Override
    public boolean canLightExit(CubeFace cubeFace) {
        return true;
    }

    @Override
    public void writeToChunkMesh(
        TerrainChunk chunk,
        Vector3i position, 
        TerrainBlockData blockData
    ) {

    }

    @Override
    public AABBf getCollider(
        AABBf collider, 
        Vector3i blockPosition
    ) {
        return collider.setMin(0f, 0f, 0f).setMax(0f, 0f, 0f);
    }

}
