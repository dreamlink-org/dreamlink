package dreamlink.zone.block.meshwriter;

import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public class PrimitiveBlockMeshWriterStrategy {

    public static PrimitiveBlockMeshWriterStrategy instance = new PrimitiveBlockMeshWriterStrategy();

    public void writeToChunkMesh(
        IZoneDirectory directory,
        TerrainChunk chunk,
        Vector3i position, 
        IPrimitiveBlock block,
        Orientation orientation
    ) {
        var terrainSystem = directory.getTerrainSystem();
        var blockData = new TerrainBlockData();
        terrainSystem.getBlockData(position, blockData);
        var positionBuffer = new Vector3f();
        var adjacentPosition = new Vector3i();
        var adjacentBlockData = new TerrainBlockData();
        for(var cubeFace : CubeFace.getCubeFaces()) {
            terrainSystem.getBlockData(
                adjacentPosition.set(position).add(cubeFace.normal),
                adjacentBlockData
            );

            if(adjacentBlockData.blockID == block.getBlockID()) {
                continue;
            }

            var remapped = orientation.remap(cubeFace);
            var mesh = block.isTransparent() ? chunk.transparentMesh : chunk.opaqueMesh;
            var originalSample = block.getTextureSample(position, remapped);
            var sample = originalSample == null ? EntityTextureSample.missing : originalSample;

            var localLight = adjacentBlockData.localLight;
            var portalLight = adjacentBlockData.portalLight;
            if(block.canLightExit(cubeFace)) {
                localLight = Math.max(localLight, blockData.localLight);
                portalLight = Math.max(portalLight, blockData.portalLight);
            }

            mesh.addQuad(
                positionBuffer.set(position),
                Vector3fMaths.one,
                cubeFace,
                orientation,
                sample,
                localLight,
                portalLight,
                originalSample == null || block.isHidden(),
                originalSample != null && block.isAffectedByLight()
            );
        }
    }
    
}
