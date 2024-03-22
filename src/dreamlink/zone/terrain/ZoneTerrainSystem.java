package dreamlink.zone.terrain;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.joml.Vector3i;

import dreamlink.logger.Logger;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3iMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.block.BarrierBlock;
import dreamlink.zone.block.DoorBlock;
import dreamlink.zone.block.SpeakerBlock;
import dreamlink.zone.terrain.light.TerrainLightPropagator;
import dreamlink.zone.terrain.light.TerrainLightResector;

public class ZoneTerrainSystem {

    private static Path terrainPath = Paths.get(".gen/chunks.dat.gz");

    private IZoneDirectory directory;

    public TerrainDirtyChunkProcessor dirtyChunkProcessor;
    public TerrainLightPropagator lightPropagator;
    public TerrainLightResector lightResector;

    private TerrainChunk[] chunks;
    public Vector3i dimensions;

    public ZoneTerrainSystem(IZoneDirectory directory) {
        this.directory = directory;
        this.dirtyChunkProcessor = new TerrainDirtyChunkProcessor(directory);
        this.lightPropagator = new TerrainLightPropagator(directory);
        this.lightResector = new TerrainLightResector(directory);
    }

    public void loadData(Path zonePath) {
        var globalConfig = this.directory.getGlobalConfigSystem();
        this.dimensions = new Vector3i()
            .set(globalConfig.chunkSpaceDimensions)
            .mul(TerrainChunk.chunkDimensions);

        var chunkPosition = new Vector3i();
        var numChunks = Vector3iMaths.getVolume(globalConfig.chunkSpaceDimensions);
        this.chunks = new TerrainChunk[numChunks];

        for(var ix = 0; ix < this.chunks.length; ix += 1) {
            Vector3iMaths.unpack(chunkPosition, ix, globalConfig.chunkSpaceDimensions);
            chunkPosition.mul(TerrainChunk.chunkDimensions);
            var chunk = new TerrainChunk(chunkPosition);
            this.chunks[ix] = chunk;
            this.dirtyChunkProcessor.addDirtyChunk(chunk);
        }

        var terrainFile = zonePath
            .resolve(ZoneTerrainSystem.terrainPath)
            .toFile();

        if(!terrainFile.exists()) {
            return;
        }

        var blockData = new TerrainBlockData();
        var blockPosition = new Vector3i();
        var totalNumBlocks = Vector3iMaths.getVolume(this.dimensions);
        var blocksPerChunk = TerrainChunk.blockCount;

        var blockSystem = this.directory.getBlockSystem();
        var doorSystem = this.directory.getDoorSystem();
        var speakerSystem = this.directory.getSpeakerSystem();

        Logger.instance.debug(String.format("Loading terrain data: %s", terrainFile.getName()));
        try(
            var fileInputStream = new FileInputStream(terrainFile);
            var bufferedInputStream = new BufferedInputStream(fileInputStream);
            var gzipInputStream = new GZIPInputStream(bufferedInputStream);
            var dataInputStream = new DataInputStream(gzipInputStream);
        ) {
            for(var ix = 0; ix < totalNumBlocks; ix += 1) {
                var blockIndex = ix % blocksPerChunk;
                var chunkIndex = ix / blocksPerChunk;
                var chunk = this.chunks[chunkIndex];

                Vector3iMaths.unpack(blockPosition, blockIndex, TerrainChunk.chunkDimensions);
                blockPosition.add(chunk.position);

                // Masking with 0xFF to convert to unsigned byte.
                short compressedBlockData = (short)(dataInputStream.readByte() & 0xFF);
                blockData.unpack(compressedBlockData);

                var block = blockSystem.getBlockByID(blockData.blockID);
                if(block == null) {
                    continue;
                }

                if(block instanceof DoorBlock && doorSystem.getDoorByPosition(blockPosition) == null) {
                    continue;
                }

                if(block instanceof SpeakerBlock && speakerSystem.getSpeakerByPosition(blockPosition) == null) {
                    continue;
                }

                blockData.localLight = block.getLocalLight();
                this.setBlockData(blockPosition, blockData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveData(Path zonePath) {
        var terrainFile = zonePath
            .resolve(ZoneTerrainSystem.terrainPath)
            .toFile();

        terrainFile.getParentFile().mkdirs();

        try(
            var fileOutputStream = new FileOutputStream(terrainFile);
            var bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            var gzipOutputStream = new GZIPOutputStream(bufferedOutputStream);
            var dataOutputStream = new DataOutputStream(gzipOutputStream);
        ) {
            for(var chunk : this.chunks) {
                for(var ix = 0; ix < TerrainChunk.blockCount; ix += 1) {
                    var compressedData = chunk.getData(ix) & 0xFF;
                    dataOutputStream.writeByte(compressedData);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       
    }

    public TerrainBlockData getBlockData(Vector3i position, TerrainBlockData blockData) {
        if(position.x <= 0 || position.x >= this.dimensions.x - 1) {
            return blockData.unpack(BarrierBlock.blockID);
        } else if(position.y <= 0 || position.y >= this.dimensions.y - 1) {
            return blockData.unpack(BarrierBlock.blockID);
        } else if(position.z <= 0 || position.z >= this.dimensions.z - 1) {
            return blockData.unpack(BarrierBlock.blockID);
        }

        var globalConfig = this.directory.getGlobalConfigSystem();
        var chunkPosition = Vector3iMaths.div(new Vector3i(position), TerrainChunk.chunkDimensions);
        var chunk = this.chunks[Vector3iMaths.pack(chunkPosition, globalConfig.chunkSpaceDimensions)];

        var blockPosition = Vector3iMaths.mod(new Vector3i(position), TerrainChunk.chunkDimensions);
        return blockData.unpack(chunk.getData(Vector3iMaths.pack(blockPosition, TerrainChunk.chunkDimensions)));
    }

    public void setBlockDataDirect(Vector3i position, TerrainBlockData blockData) {
        if(position.x <= 0 || position.x >= this.dimensions.x - 1) {
            return;
        } else if(position.y <= 0 || position.y >= this.dimensions.y - 1) {
            return;
        } else if(position.z <= 0 || position.z >= this.dimensions.z - 1) {
            return;
        }

        var globalConfig = this.directory.getGlobalConfigSystem();
        var chunkPosition = Vector3iMaths.div(new Vector3i(position), TerrainChunk.chunkDimensions);
        var chunk = this.chunks[Vector3iMaths.pack(chunkPosition, globalConfig.chunkSpaceDimensions)];

        var blockPosition = Vector3iMaths.mod(new Vector3i(position), TerrainChunk.chunkDimensions);
        var oldValue = chunk.getData(Vector3iMaths.pack(blockPosition, TerrainChunk.chunkDimensions));
        var newValue = blockData.pack();

        if(oldValue == newValue) {
            return;
        }

        chunk.setData(Vector3iMaths.pack(blockPosition, TerrainChunk.chunkDimensions), newValue);

        for(var cubeFace : CubeFace.getCubeFaces()) {
            blockPosition.set(cubeFace.normal).add(position);
            if(position.x < 0 || position.x > this.dimensions.x - 1) {
                continue;
            } else if(position.y < 0 || position.y > this.dimensions.y - 1) {
                continue;
            } else if(position.z < 0 || position.z > this.dimensions.z - 1) {
                continue;
            }

            var adjacentChunkPosition = Vector3iMaths.div(blockPosition, TerrainChunk.chunkDimensions);
            var adjacentChunkIndex = Vector3iMaths.pack(adjacentChunkPosition, globalConfig.chunkSpaceDimensions);
            this.dirtyChunkProcessor.addDirtyChunk(this.chunks[adjacentChunkIndex]);
        }
    }

    public void setBlockData(Vector3i position, TerrainBlockData blockData) {
        var oldBlockData = this.getBlockData(position, new TerrainBlockData());
        this.lightResector.addLightResection(position, oldBlockData);
        oldBlockData.localLight = 0;
        oldBlockData.portalLight = 0;
        this.setBlockDataDirect(position, oldBlockData);

        this.lightResector.processOutstandingLightingTasks();
        this.setBlockDataDirect(position, blockData);
        this.lightPropagator.addLightPropagation(position);
        this.lightPropagator.processOutstandingLightingTasks();
    }

    public boolean updateDirtyChunks() {
        if(this.dirtyChunkProcessor.hasOutstandingWork()) {
            this.dirtyChunkProcessor.doWork();
            return true;
        }
        return false;
    }

    public void destroy() {
        if(this.chunks != null) {
            Logger.instance.debug("Destroying terrain chunks");
            for(var chunk : this.chunks) {
                chunk.destroy();
            }
        }
    }

    public void renderOpaque() {
        for(var chunk : this.chunks) {
            chunk.opaqueMesh.render();
        }
    }

    public void renderTransparent() {
        for(var chunk : this.chunks) {
            chunk.transparentMesh.render();
        }
    }
    
}
