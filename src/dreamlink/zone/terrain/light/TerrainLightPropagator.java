package dreamlink.zone.terrain.light;

import java.util.ArrayDeque;
import java.util.Queue;

import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.terrain.TerrainBlockData;

public class TerrainLightPropagator {

    private IZoneDirectory directory;
    private Queue<LightVoxel> workQueue;

    public TerrainLightPropagator(IZoneDirectory directory) {
        this.directory = directory;
        this.workQueue = new ArrayDeque<>();
    }

    private void processLightPropagation(LightVoxel sourceVoxel) {
        var adjacentPosition = new Vector3i();
        var adjacentBlockData = new TerrainBlockData();
        var terrainSystem = this.directory.getTerrainSystem();
        var blockSystem = this.directory.getBlockSystem();
        var blockData = terrainSystem.getBlockData(sourceVoxel.position, new TerrainBlockData());
        var block = blockSystem.getBlockByID(blockData.blockID);

        for(var cubeFace : CubeFace.getCubeFaces()) {
            adjacentPosition.set(sourceVoxel.position).add(cubeFace.normal);
            terrainSystem.getBlockData(adjacentPosition, adjacentBlockData);
            var adjacentBlock = blockSystem.getBlockByID(adjacentBlockData.blockID);

            var orientation = Orientation.getOrientation(blockData.orientationID);
            var remappedSourceOrientation = orientation.remap(cubeFace);
            var adjacentOrientation = Orientation.getOrientation(adjacentBlockData.orientationID);
            var remappedDestinationOrientation = adjacentOrientation.remap(cubeFace);

            var canTransmit = true
                && block.canLightExit(remappedSourceOrientation)
                && adjacentBlock.canLightEnter(remappedDestinationOrientation);

            if(!canTransmit) {
                continue;
            }

            var isFinalized = true;
            var voxel = new LightVoxel();
            voxel.position.set(adjacentPosition);

            for(var lightType : LightType.values()) {
                voxel.setLight(lightType, 0);

                if(sourceVoxel.getLight(lightType) == 0) {
                    continue;
                }

                var voxelLight = sourceVoxel.getLight(lightType);
                var transmittedLight = voxelLight - 1;

                var blockLight = adjacentBlockData.getLight(lightType);
                if(transmittedLight <= blockLight) {
                    continue;
                }

                isFinalized = false;
                adjacentBlockData.setLight(lightType, transmittedLight);
                voxel.setLight(lightType, transmittedLight);
            }

            if(isFinalized) {
                continue;
            }

            this.workQueue.add(voxel);
            terrainSystem.setBlockDataDirect(adjacentPosition, adjacentBlockData);
        }
    }

    public void addLightPropagation(Vector3i position) {
        var blockData = this.directory.getTerrainSystem().getBlockData(position, new TerrainBlockData());
        var voxel = new LightVoxel();
        voxel.position.set(position);
        voxel.localLight = blockData.localLight;
        voxel.portalLight = blockData.portalLight;
        this.workQueue.add(voxel);
    }

    public void processOutstandingLightingTasks() {
        while(true) {
            if(!this.workQueue.isEmpty()) {
                var voxel = this.workQueue.remove();
                this.processLightPropagation(voxel);
                continue;
            }
            break;
        }
    }
    
}
