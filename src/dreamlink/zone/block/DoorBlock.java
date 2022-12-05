package dreamlink.zone.block;

import org.joml.AABBf;
import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.block.meshwriter.DoorBlockMeshWriterStrategy;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public class DoorBlock implements IBlock {

    private static int blockID = 0x02;
    private static String blockName = "door";

    private IZoneDirectory directory;

    public DoorBlock(IZoneDirectory directory) {
        this.directory = directory;
    }

    @Override
    public int getBlockID() {
        return DoorBlock.blockID;
    }

    @Override
    public String getBlockName() {
        return DoorBlock.blockName;
    }

    @Override
    public int getLocalLight() {
        return 0;
    }

    @Override
    public void writeToChunkMesh(
        TerrainChunk chunk,
        Vector3i position, 
        TerrainBlockData blockData
    ) {
        DoorBlockMeshWriterStrategy.instance.writeToChunkMesh(
            this.directory,
            chunk, 
            position, 
            blockData
        );
    }

    @Override
    public boolean canLightEnter(CubeFace cubeFace) {
        return cubeFace != CubeFace.front;
    }

    @Override
    public boolean canLightExit(CubeFace cubeFace) {
        return cubeFace != CubeFace.back;
    }

    @Override
    public AABBf getCollider(
        AABBf collider, 
        Vector3i blockPosition
    ) {
        var door = this.directory.getDoorSystem().getDoorByPosition(blockPosition);
        return door.getCollider(collider);
    }

}
