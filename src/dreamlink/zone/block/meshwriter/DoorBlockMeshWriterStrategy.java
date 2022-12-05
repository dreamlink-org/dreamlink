package dreamlink.zone.block.meshwriter;

import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.PortalTextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;
import dreamlink.zone.terrain.light.LightType;

public class DoorBlockMeshWriterStrategy {

    private static float epsilon = 0.01f;

    public static DoorBlockMeshWriterStrategy instance = new DoorBlockMeshWriterStrategy();

    public void writeToChunkMesh(
        IZoneDirectory directory,
        TerrainChunk chunk,
        Vector3i position,
        TerrainBlockData blockData
    ) {
        var doorSystem = directory.getDoorSystem();
        var door = doorSystem.getDoorByPosition(position);
        if(door == null) {
            return;
        }

        var textureSample = position.equals(door.position)
            ? door.bottomBlockTextureSample
            : door.topBlockTextureSample;
        textureSample = textureSample == null ? EntityTextureSample.missing : textureSample;

        var adjacentPosition = new Vector3i();
        var adjacentBlockData = new TerrainBlockData();
        var orientation = Orientation.getOrientation(blockData.orientationID);
        var orientationCubeFace = orientation.cubeFace;
        var normalSignum = Vector3fMaths.dot(Vector3fMaths.one, orientationCubeFace.normal);

        var dimensionsBuffer = new Vector3f(1f);
        Vector3fMaths.setAxisValue(dimensionsBuffer, orientationCubeFace.axisID, 0.5f);

        var positionBuffer = new Vector3f();
        Vector3fMaths.setAxisValue(positionBuffer, orientationCubeFace.axisID, 0.25f * (1 - normalSignum));
        Vector3fMaths.add(positionBuffer, position);

        var isDoorOpen = doorSystem.openDoor == door;
        var mesh = chunk.opaqueMesh;

        for(var cubeFace : CubeFace.getCubeFaces()) {
            adjacentPosition
                .set(position)
                .add(cubeFace.normal);

            directory.getTerrainSystem().getBlockData(adjacentPosition, adjacentBlockData);

            if(isDoorOpen) {
                var minusEpsilon = DoorBlockMeshWriterStrategy.epsilon - 1f;
                var innerPortalPosition = new Vector3f(positionBuffer).add(
                    cubeFace.normal.x * dimensionsBuffer.x * minusEpsilon,
                    cubeFace.normal.y * dimensionsBuffer.y * minusEpsilon,
                    cubeFace.normal.z * dimensionsBuffer.z * minusEpsilon
                );

                mesh.addQuad(
                    innerPortalPosition,
                    dimensionsBuffer,
                    cubeFace,
                    orientation,
                    PortalTextureSample.instance,
                    0xF, 
                    0x0,
                    false,
                    false
                );
            }

            // Multiple doors of the same block shouldn't "merge" together.
            // Just merge blocks that form part of the same door.
            var adjacentDoor = doorSystem.getDoorByPosition(adjacentPosition);
            if(adjacentDoor == door) {
                continue;
            }

            var remapped = orientation.remap(cubeFace);
            if(remapped != CubeFace.front) {
                mesh.addQuad(
                    positionBuffer,
                    dimensionsBuffer,
                    cubeFace,
                    orientation,
                    EntityTextureSample.doorSide,
                    0x0,
                    0x0,
                    false,
                    false
                );
                continue;
            }

            if(isDoorOpen) {
                mesh.addQuad(
                    positionBuffer,
                    dimensionsBuffer,
                    cubeFace,
                    orientation,
                    PortalTextureSample.instance,
                    0x0, 
                    0x0,
                    false,
                    false
                );
                continue;
            }

            mesh.addQuad(
                positionBuffer,
                dimensionsBuffer,
                cubeFace,
                orientation,
                textureSample,
                adjacentBlockData.getLight(LightType.local),
                adjacentBlockData.getLight(LightType.portal),
                false,
                true
            );
        }
    }
    
}
