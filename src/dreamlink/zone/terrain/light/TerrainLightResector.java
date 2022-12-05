package dreamlink.zone.terrain.light;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.joml.Vector3i;

import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.terrain.TerrainBlockData;

public class TerrainLightResector {

    private IZoneDirectory directory; // Changed from Zone to IZoneDirectory
    private Queue<LightVoxel> workQueue;
    private List<Vector3i> resectionBarrier;

    public TerrainLightResector(IZoneDirectory directory) { // Parameter type changed
        this.directory = directory;
        this.workQueue = new ArrayDeque<>();
        this.resectionBarrier = new ArrayList<>();
    }

    private void addToResectionBarrier(Vector3i position) {
        this.resectionBarrier.add(new Vector3i(position));
    }

    private void processLightResection(LightVoxel sourceVoxel) {
        var adjacentPosition = new Vector3i();
        var adjacentBlockData = new TerrainBlockData();
        // Use the directory to get terrain and block modules
        var terrainSystem = this.directory.getTerrainSystem();
        var blockSystem = this.directory.getBlockSystem();
        var blockData = terrainSystem.getBlockData(sourceVoxel.position, new TerrainBlockData());
        var block = blockSystem.getBlockByID(blockData.blockID);

        for (var cubeFace : CubeFace.getCubeFaces()) {
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

            if (!canTransmit) {
                this.addToResectionBarrier(adjacentPosition);
                continue;
            }

            var isFinalized = true;
            var voxel = new LightVoxel();
            voxel.position.set(adjacentPosition);

            for (var lightType : LightType.values()) {
                voxel.setLight(lightType, -1);

                if (sourceVoxel.getLight(lightType) < 0) {
                    continue;
                }

                var blockLight = adjacentBlockData.getLight(lightType);
                var voxelLight = sourceVoxel.getLight(lightType);
                var transmittedLight = voxelLight - 1;

                if (blockLight == 0) {
                    continue;
                }

                if (transmittedLight < blockLight) {
                    this.addToResectionBarrier(adjacentPosition);
                    continue;
                }

                isFinalized = false;
                adjacentBlockData.setLight(lightType, 0);
                voxel.setLight(lightType, blockLight);
            }

            if (isFinalized) {
                continue;
            }

            this.workQueue.add(voxel);
            terrainSystem.setBlockDataDirect(adjacentPosition, adjacentBlockData);
        }
    }

    public void addLightResection(Vector3i position, TerrainBlockData oldBlockData) {
        var voxel = new LightVoxel();
        voxel.position.set(position);
        voxel.localLight = oldBlockData.localLight;
        voxel.portalLight = oldBlockData.portalLight;
        this.workQueue.add(voxel);
    }

    public void processOutstandingLightingTasks() {
        while (true) {
            if (!this.workQueue.isEmpty()) {
                var lightVoxel = this.workQueue.remove();
                this.processLightResection(lightVoxel);
                continue;
            }

            if (!this.resectionBarrier.isEmpty()) {
                for (var position : this.resectionBarrier) {
                    this.directory.getTerrainSystem().lightPropagator.addLightPropagation(position);
                }
                this.resectionBarrier.clear();
                continue;
            }

            break;
        }
    }
}