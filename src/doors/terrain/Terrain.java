package doors.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.joml.Vector3i;

import doors.utility.VectorSpace;
import doors.graphics.MeshBuffer;
import doors.utility.CubeFace;
import doors.utility.VectorExtension;

public class Terrain {

    public Map<Integer, Block> blockMap;
    public VectorSpace space;
    private VectorSpace chunkSpace;
    private MeshBuffer meshBuffer;

    private Chunk[] chunks;
    private Queue<Chunk> dirtyChunks = new LinkedList<>();

    public Terrain(Vector3i chunkDimensions) {
        this.blockMap = new HashMap<>();
        this.space = new VectorSpace(new Vector3i(chunkDimensions).mul(Chunk.CHUNK_DIMENSIONS));
        this.chunkSpace = new VectorSpace(chunkDimensions);

        this.chunks = new Chunk[this.chunkSpace.getMaxIndex()];
        for(var ix = 0; ix < this.chunks.length; ix += 1) {
            var chunk = new Chunk();
            chunk.position = this.chunkSpace.fromIndex(ix).mul(Chunk.CHUNK_DIMENSIONS);
            this.chunks[ix] = chunk;
        }

        var maxQuads = new VectorSpace(Chunk.CHUNK_DIMENSIONS).getMaxIndex();
        this.meshBuffer = new MeshBuffer(maxQuads);
    }

    public void readBlocks(DataInputStream stream) throws IOException {
        for(var chunk : this.chunks) {
            for(var ix = 0; ix < chunk.blockData.length; ix += 1) {
                chunk.blockData[ix] = stream.readInt();
            }
        }
    }

    public void writeBlocks(DataOutputStream stream) throws IOException {
        for(var chunk : this.chunks) {
            for(var ix = 0; ix < chunk.blockData.length; ix += 1) {
                stream.write(chunk.blockData[ix]);
            }
        }
    }

    public Block getBlock(Vector3i position) {
        if(!this.space.isWithinBounds(position)) {
            return null;
        }

        var chunkPosition = VectorExtension.div(new Vector3i(position), Chunk.CHUNK_DIMENSIONS);
        var blockPosition = new Vector3i(chunkPosition).mul(Chunk.CHUNK_DIMENSIONS).mul(-1).add(position);

        var chunkIndex = this.chunkSpace.getIndex(chunkPosition);
        var chunk = this.chunks[chunkIndex];

        var blockID = chunk.getBlockID(blockPosition);
        return blockID == 0 ? null : this.blockMap.get(blockID);
    }


    public void setBlock(Vector3i position, Block block) {
        if(!this.space.isWithinBounds(position)) {
            return;
        }

        var chunkPosition = VectorExtension.div(new Vector3i(position), Chunk.CHUNK_DIMENSIONS);
        var blockPosition = new Vector3i(chunkPosition).mul(Chunk.CHUNK_DIMENSIONS).mul(-1).add(position);

        var chunkIndex = this.chunkSpace.getIndex(chunkPosition);
        var chunk = this.chunks[chunkIndex];

        var madeDirty = chunk.setBlockID(blockPosition, block == null ? 0 : block.blockID);
        if(madeDirty) {
            this.dirtyChunks.add(chunk);
        }
    }

    public boolean hasDirtyChunks() {
        return this.dirtyChunks.size() > 0;
    }

    public void processDirtyChunk() {
        this.meshBuffer.clear();

        var chunk = this.dirtyChunks.remove();
        var maxIndex = chunk.blockSpace.getMaxIndex();
        for(var blockIndex = 0; blockIndex < maxIndex; blockIndex += 1) {
            var localBlockPosition = chunk.blockSpace.fromIndex(blockIndex);
            var globalBlockPosition = new Vector3i(localBlockPosition).add(chunk.position);
            var block = this.getBlock(globalBlockPosition);

            if(block == null) {
                continue;
            }

            for(var cubeFace : CubeFace.CUBE_FACES) {
                var adjacentPosition = new Vector3i(globalBlockPosition).add(cubeFace.normal);
                var adjacentBlock = this.getBlock(adjacentPosition);

                if(adjacentBlock != null) {
                    continue;
                }

                for(var ix = 0; ix < cubeFace.vertices.length; ix += 1) {
                    var vertex = cubeFace.vertices[ix];
                    this.meshBuffer.position.set(localBlockPosition).add(vertex);
                    this.meshBuffer.textureOffset.set(block.textureSample.textureOffsets[ix]);
                    this.meshBuffer.normal.set(cubeFace.normal);
                    this.meshBuffer.push();
                }

            }
        }
    }

}
