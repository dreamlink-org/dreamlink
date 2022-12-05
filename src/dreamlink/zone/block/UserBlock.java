package dreamlink.zone.block;

import org.joml.AABBf;
import org.joml.Vector2i;
import org.joml.Vector3i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Orientation;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.zone.IStamp;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.block.meshwriter.IPrimitiveBlock;
import dreamlink.zone.block.meshwriter.PrimitiveBlockMeshWriterStrategy;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.TerrainChunk;

public class UserBlock implements IPrimitiveBlock, IStamp {

    private class InternalStampSpriteTemplate implements ISpriteTemplate {

        @Override
        public void writeToSpriteBatch(
            SpriteBatch spriteBatch, 
            Vector2i position, 
            Vector2i dimensions, 
            SpriteHeight height
        ) {
            var sample = UserBlock.this.textureSamples[CubeFace.front.cubeFaceID];
            sample = sample == null ? EntityTextureSample.missing : sample;
            spriteBatch.writeTextureSample(
                position, 
                dimensions, 
                height, 
                sample,
                Vector4fMaths.white
            );
        }

    }

    private static String stampType = "Block";

    private IZoneDirectory directory;
    private TextureSample[] textureSamples;

    private int blockID;
    public String blockName;
    public int lightLevel;
    public boolean isTransparent;
    public boolean isTransmittingLight;
    public boolean isAffectedByLight;
    public boolean isHidden;
    public BlockMaterial blockMaterial;
    public String interactionMessage;
    private ISpriteTemplate stampSprite;
    
    public UserBlock(
        IZoneDirectory directory,
        int blockID, 
        String blockName, 
        boolean isHidden,
        boolean isTransparent,
        int lightingAmount,
        boolean isAffectedByLighting,
        boolean isTransmittingLighting,
        String interactionMessage,
        BlockMaterial blockMaterial
    ) {
        this.directory = directory;
        this.textureSamples = new TextureSample[6];
        this.stampSprite = new InternalStampSpriteTemplate();

        this.blockID = blockID;
        this.blockName = blockName;
        this.interactionMessage = interactionMessage;
        this.lightLevel = lightingAmount;
        this.isTransparent = isTransparent;
        this.isAffectedByLight = isAffectedByLighting;
        this.isTransmittingLight = isTransmittingLighting;
        this.isHidden = isHidden;
        this.blockMaterial = blockMaterial;
    }

    public void setTextureSample(CubeFace cubeFace, TextureSample textureSample) {
        this.textureSamples[cubeFace.cubeFaceID] = textureSample;
    }

    @Override
    public boolean isTransparent() {
        return this.isTransparent;
    }

    @Override
    public boolean isHidden() {
        return this.isHidden;
    }

    @Override
    public boolean isAffectedByLight() {
        return this.isAffectedByLight;
    }

    public TextureSample getTextureSample(Vector3i position,CubeFace cubeFace) {
        return this.textureSamples[cubeFace.cubeFaceID];
    }

    @Override
    public String getStampName() {
        return this.blockName;
    }

    @Override
    public String getStampType() {
        return UserBlock.stampType;
    }

    @Override
    public ISpriteTemplate getStampSprite() {
        return this.stampSprite;
    }

    @Override
    public void applyStamp(
        Vector3i position, 
        Orientation orientation
    ) {
        this.directory.getTerrainSystem().setBlockData(position, new TerrainBlockData(
            this.blockID,
            orientation.orientationID,
            this.lightLevel,
            0
        ));
    }

    @Override
    public int getBlockID() {
        return this.blockID;
    }

    @Override
    public String getBlockName() {
        return this.blockName;
    }

    @Override
    public int getLocalLight() {
        return this.lightLevel;
    }

    @Override
    public boolean canLightEnter(CubeFace cubeFace) {
        return this.isTransmittingLight;
    }

    @Override
    public boolean canLightExit(CubeFace cubeFace) {
        return true;
    }

    @Override
    public void writeToChunkMesh(
        TerrainChunk chunk,
        Vector3i position, 
        TerrainBlockData blockData
    ) {
        PrimitiveBlockMeshWriterStrategy.instance.writeToChunkMesh(
            this.directory,
            chunk,
            position, 
            this,
            Orientation.getOrientation(blockData.orientationID)
        );
    }

    @Override
    public AABBf getCollider(
        AABBf collider, 
        Vector3i blockPosition
    ) {
        return collider
            .setMin(blockPosition.x, blockPosition.y, blockPosition.z)
            .setMax(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z + 1);
    }

}

