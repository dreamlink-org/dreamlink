package dreamlink.zone.terrain;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.joml.Vector3i;

import dreamlink.utility.maths.Vector3iMaths;
import dreamlink.zone.IZoneDirectory;

public class TerrainDirtyChunkProcessor {

    private IZoneDirectory directory;
    private Queue<TerrainChunk> dirtyChunks;
    private Set<TerrainChunk> dirtyChunksSet;

    public TerrainDirtyChunkProcessor(IZoneDirectory directory) {
        this.directory = directory;
        this.dirtyChunks = new ArrayDeque<>();
        this.dirtyChunksSet = new HashSet<>();
    }

    public void addDirtyChunk(TerrainChunk chunk) {
        if(this.dirtyChunksSet.contains(chunk)) {
            return;
        }
        this.dirtyChunks.add(chunk);
        this.dirtyChunksSet.add(chunk);
    }

    public boolean hasOutstandingWork() {
        return !this.dirtyChunks.isEmpty();
    }

    public void doWork() {
        var chunk = this.dirtyChunks.remove();
        this.dirtyChunksSet.remove(chunk);

        if(!chunk.isSetup()) {
            chunk.setup();
        }

        var position = new Vector3i();
        var blockData = new TerrainBlockData();
        var terrainSystem = this.directory.getTerrainSystem();
        var blockSystem = this.directory.getBlockSystem();

        chunk.opaqueMesh.clear();
        chunk.transparentMesh.clear();
        for(var ix = 0; ix < TerrainChunk.blockCount; ix += 1) {
            Vector3iMaths.unpack(position, ix, TerrainChunk.chunkDimensions);
            position.add(chunk.position);
                
            terrainSystem.getBlockData(position, blockData);
            blockSystem.getBlockByID(blockData.blockID).writeToChunkMesh(
                chunk, 
                position, 
                blockData
            );
        }
        chunk.opaqueMesh.buffer();
        chunk.transparentMesh.buffer();
    }
    
}
