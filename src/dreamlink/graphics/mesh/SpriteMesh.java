package dreamlink.graphics.mesh;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dreamlink.graphics.mesh.strategy.color.MeshColorQuadStrategy;
import dreamlink.graphics.mesh.strategy.index.MeshIndexQuadStrategy;
import dreamlink.graphics.mesh.strategy.position.MeshScreenPositionQuadStrategy;
import dreamlink.graphics.mesh.strategy.texture.IMeshTextureStrategyProvider;
import dreamlink.graphics.mesh.strategy.texture.MeshTextureQuadStrategy;
import dreamlink.graphics.mesh.vertexbuffer.MeshFloatBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIndexBuffer;
import dreamlink.graphics.mesh.vertexbuffer.MeshIntegerBuffer;
import dreamlink.graphics.program.SpriteShaderProgram;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.graphics.texture.sample.ZoneTextureSample;

public class SpriteMesh extends Mesh {

    private class InternalTextureStrategyProvider implements IMeshTextureStrategyProvider {

        @Override
        public MeshFloatBuffer getTextureOffsetBuffer() {
            return SpriteMesh.this.textureOffsetBuffer;
        }

        @Override
        public MeshIntegerBuffer getTextureMetaDataBuffer() {
            return SpriteMesh.this.textureMetaDataBuffer;
        }

    }

    private static int positionLocation = 0;
    private static int positionSize = 2;

    private static int textureOffsetLocation = 1;
    private static int textureOffsetSize = 4;

    private static int textureMetaDataLocation = 2;
    private static int textureMetaDataSize = 1;

    private static int colorLocation = 3;
    private static int colorSize = 1;

    private MeshIndexBuffer indexBuffer;
    private MeshFloatBuffer positionBuffer;
    private MeshFloatBuffer textureOffsetBuffer;
    private MeshIntegerBuffer textureMetaDataBuffer;
    private MeshIntegerBuffer colorBuffer;

    private MeshIndexQuadStrategy indexStrategy;
    private MeshScreenPositionQuadStrategy positionStrategy;
    private MeshTextureQuadStrategy textureStrategy;
    private MeshColorQuadStrategy colorStrategy;

    public SpriteMesh() {
        this.indexBuffer = new MeshIndexBuffer();
        this.positionBuffer = new MeshFloatBuffer(SpriteMesh.positionLocation, SpriteMesh.positionSize);
        this.textureOffsetBuffer = new MeshFloatBuffer(SpriteMesh.textureOffsetLocation, SpriteMesh.textureOffsetSize);
        this.textureMetaDataBuffer = new MeshIntegerBuffer(SpriteMesh.textureMetaDataLocation, SpriteMesh.textureMetaDataSize);
        this.colorBuffer = new MeshIntegerBuffer(SpriteMesh.colorLocation, SpriteMesh.colorSize);

        this.indexStrategy = new MeshIndexQuadStrategy(this::getIndexBuffer);
        this.positionStrategy = new MeshScreenPositionQuadStrategy(this::getPositionBuffer);
        this.textureStrategy = new MeshTextureQuadStrategy(new InternalTextureStrategyProvider());
        this.colorStrategy = new MeshColorQuadStrategy(this::getColorBuffer);

        this.addMeshBuffer(this.indexBuffer);
        this.addMeshBuffer(this.positionBuffer);
        this.addMeshBuffer(this.textureOffsetBuffer);
        this.addMeshBuffer(this.textureMetaDataBuffer);
        this.addMeshBuffer(this.colorBuffer);
    }

    @Override
    protected int getIndicesCount() {
        return this.indexBuffer.getIndicesCount();
    }

    private MeshIndexBuffer getIndexBuffer() {
        return this.indexBuffer;
    }

    private MeshFloatBuffer getPositionBuffer() {
        return this.positionBuffer;
    }

    private MeshIntegerBuffer getColorBuffer() {
        return this.colorBuffer;
    }

    public void addQuad(
        Vector2f position,
        Vector2f dimensions,
        TextureSample textureSample,
        Vector4f color
    ) {
        if(textureSample == null) {
            return;
        }

        int textureUnitID;
        if(textureSample instanceof EntityTextureSample) {
            textureUnitID = SpriteShaderProgram.entityTextureUnitID;
        } else if(textureSample instanceof OverlayTextureSample) {
            textureUnitID = SpriteShaderProgram.overlayTextureUnitID;
        } else if(textureSample instanceof ZoneTextureSample) {
            textureUnitID = SpriteShaderProgram.zoneTextureUnitID;
        } else {
            throw new IllegalArgumentException("Unknown texture sample type");
        }

        this.indexStrategy.add();
        this.positionStrategy.add(position, dimensions);
        this.textureStrategy.add(textureUnitID, 0, textureSample);
        this.colorStrategy.add(color);  
    }
    
}
