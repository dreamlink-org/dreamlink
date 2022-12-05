package dreamlink.graphics.mesh;

import org.joml.Vector3f;

import dreamlink.graphics.mesh.strategy.index.MeshIndexQuadStrategy;
import dreamlink.graphics.mesh.strategy.position.MeshWorldPositionQuadStrategy;
import dreamlink.graphics.mesh.strategy.terrain.MeshTerrainQuadStrategy;
import dreamlink.graphics.mesh.strategy.texture.IMeshTextureStrategyProvider;
import dreamlink.graphics.mesh.strategy.texture.MeshTextureQuadStrategy;
import dreamlink.graphics.mesh.vertexbuffer.MeshFloatBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIndexBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIntegerBuffer;
import dreamlink.graphics.program.OpaqueShaderProgram;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.PortalTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.graphics.texture.sample.ZoneTextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;

public class TerrainMesh extends Mesh {

    private class InternalTextureStrategyProvider implements IMeshTextureStrategyProvider {

        @Override
        public MeshFloatBuffer getTextureOffsetBuffer() {
            return TerrainMesh.this.textureOffsetBuffer;
        }

        @Override
        public MeshIntegerBuffer getTextureMetaDataBuffer() {
            return TerrainMesh.this.textureMetaDataBuffer;
        }

    }

    private static int positionLocation = 0;
    private static int positionSize = 3;

    private static int textureOffsetLocation = 1;
    private static int textureOffsetSize = 4;

    private static int textureMetaDataLocation = 2;
    private static int textureMetaDataSize = 1;

    private static int terrainMetaDataLocation = 3;
    private static int terrainMetaDataSize = 1;

    private MeshIndexBuffer indexBuffer;
    private MeshFloatBuffer positionBuffer;
    private MeshFloatBuffer textureOffsetBuffer;
    private MeshIntegerBuffer textureMetaDataBuffer;
    private MeshIntegerBuffer terrainMetaDataBuffer;

    private MeshIndexQuadStrategy indexStrategy;
    private MeshWorldPositionQuadStrategy positionStrategy;
    private MeshTextureQuadStrategy textureStrategy;
    private MeshTerrainQuadStrategy terrainStrategy;

    public TerrainMesh() {
        this.indexBuffer = new MeshIndexBuffer();
        this.positionBuffer = new MeshFloatBuffer(TerrainMesh.positionLocation, TerrainMesh.positionSize);
        this.textureOffsetBuffer = new MeshFloatBuffer(TerrainMesh.textureOffsetLocation, TerrainMesh.textureOffsetSize);
        this.textureMetaDataBuffer = new MeshIntegerBuffer(TerrainMesh.textureMetaDataLocation, TerrainMesh.textureMetaDataSize);
        this.terrainMetaDataBuffer = new MeshIntegerBuffer(TerrainMesh.terrainMetaDataLocation, TerrainMesh.terrainMetaDataSize);

        this.indexStrategy = new MeshIndexQuadStrategy(this::getIndexBuffer);
        this.positionStrategy = new MeshWorldPositionQuadStrategy(this::getPositionBuffer);
        this.textureStrategy = new MeshTextureQuadStrategy(new InternalTextureStrategyProvider());
        this.terrainStrategy = new MeshTerrainQuadStrategy(this::getTerrainMetaDataBuffer);

        this.addMeshBuffer(this.indexBuffer);
        this.addMeshBuffer(this.positionBuffer);
        this.addMeshBuffer(this.textureOffsetBuffer);
        this.addMeshBuffer(this.textureMetaDataBuffer);
        this.addMeshBuffer(this.terrainMetaDataBuffer);
    }

    private MeshIndexBuffer getIndexBuffer() {
        return this.indexBuffer;
    }

    private MeshFloatBuffer getPositionBuffer() {
        return this.positionBuffer;
    }

    private MeshIntegerBuffer getTerrainMetaDataBuffer() {
        return this.terrainMetaDataBuffer;
    }

    @Override
    protected int getIndicesCount() {
        return this.indexBuffer.getIndicesCount();
    }

    public void addQuad(
        Vector3f position,
        Vector3f dimensions,
        CubeFace cubeFace,
        Orientation orientation,
        TextureSample textureSample,
        int localLight,
        int portalLight,
        boolean isHidden,
        boolean isAffectedByLight
    ) {
        int textureUnitID;
        if(textureSample instanceof EntityTextureSample) {
            textureUnitID = OpaqueShaderProgram.entityTextureUnitID;
        } else if(textureSample instanceof ZoneTextureSample) {
            textureUnitID = OpaqueShaderProgram.zoneTextureUnitID;
        } else if(textureSample instanceof PortalTextureSample) {
            textureUnitID = OpaqueShaderProgram.portalTextureUnitID;
        } else {
            throw new IllegalArgumentException("Unknown texture sample type");
        }

        var windingOffset = cubeFace.axisID == 1
            ? orientation.orientationID
            : 0;

        this.indexStrategy.add();
        this.positionStrategy.add(position, dimensions, cubeFace);
        this.textureStrategy.add(textureUnitID, windingOffset, textureSample);
        this.terrainStrategy.add(localLight, portalLight, isHidden, isAffectedByLight);
    }

}

