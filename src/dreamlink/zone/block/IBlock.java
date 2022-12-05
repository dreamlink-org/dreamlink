package dreamlink.zone.block;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public interface IBlock {

    public int getBlockID();

    public String getBlockName();

    public int getLocalLight();

    public boolean canLightEnter(CubeFace cubeFace);

    public boolean canLightExit(CubeFace cubeFace);

    public AABBf getCollider(
        AABBf collider, 
        Vector3i blockPosition
    );

    public void writeToChunkMesh(
        TerrainChunk chunk,
        Vector3i position,
        TerrainBlockData blockData
    );

}
