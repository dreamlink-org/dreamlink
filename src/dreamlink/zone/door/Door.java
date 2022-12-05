package dreamlink.zone.door;

import org.joml.AABBf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.Orientation;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.zone.IStamp;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.Zone;
import dreamlink.zone.block.AirBlock;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.light.LightType;

public class Door implements IStamp {

    private class InternalDoorSprite implements ISpriteTemplate {

        @Override
        public void writeToSpriteBatch(
            SpriteBatch spriteBatch, 
            Vector2i position, 
            Vector2i dimensions, 
            SpriteHeight height
        ) {
            var sample = Door.this.topBlockTextureSample;
            sample = sample == null ? EntityTextureSample.missing : sample;

            spriteBatch.writeTextureSample(
                position, 
                dimensions,
                height,
                sample,
                Door.this.isPlaced ? Vector4fMaths.halfAlpha : Vector4fMaths.white
            );
        }

    }

    public static Vector3i[] doorBlockOffsets = new Vector3i[] {
        new Vector3i(0, 0, 0),
        new Vector3i(0, 1, 0)
    };

    private static String stampType = "Door";
    private static int openPortalLightLevel = 15;

    private ISpriteTemplate doorSprite;

    public String name;
    public String targetZoneName;
    public String targetDoorName;
    public TextureSample bottomBlockTextureSample;
    public TextureSample topBlockTextureSample;
    public IZoneDirectory directory;

    public Orientation orientation;
    public Vector3i position;
    public boolean isPlaced;

    public Door(
        IZoneDirectory directory,
        String name,
        TextureSample topBlockTextureSample,
        TextureSample bottomBlockTextureSample,
        String targetLevelName,
        String targetDoorName
    ) {
        this.directory = directory;
        this.name = name;
        this.targetZoneName = targetLevelName;
        this.targetDoorName = targetDoorName;
        this.topBlockTextureSample = topBlockTextureSample;
        this.bottomBlockTextureSample = bottomBlockTextureSample;
        this.doorSprite = new InternalDoorSprite();
        this.position = new Vector3i();
    }

    public void setPlacement(
        Vector3i position,
        Orientation orientation
    ) {
        this.position.set(position);
        this.orientation = orientation;
        this.isPlaced = true;
    }

    public Vector3f getPlanePosition(Vector3f plane) {
        plane.set(0.5f, 1f, 0.5f);
        return Vector3fMaths.add(plane, this.position);
    }

    public AABBf getCollider(AABBf collider) {
        var cubeFace = this.orientation.cubeFace;
        var normalSignum = Vector3fMaths.dot(Vector3fMaths.one, cubeFace.normal);
        var colliderNodeBuffer = new Vector3f();

        Vector3fMaths.setAxisValue(colliderNodeBuffer, cubeFace.axisID, 0.25f * (1 - normalSignum));
        Vector3fMaths.add(colliderNodeBuffer, this.position);
        collider.setMin(colliderNodeBuffer);

        colliderNodeBuffer.set(1f, 2f, 1f);
        Vector3fMaths.setAxisValue(colliderNodeBuffer, this.orientation.cubeFace.axisID, 0.5f);
        colliderNodeBuffer.add(collider.minX, collider.minY, collider.minZ);
        return collider.setMax(colliderNodeBuffer);
    }

    @Override
    public String getStampType() {
        return Door.stampType;
    }

    @Override
    public ISpriteTemplate getStampSprite() {
        return this.doorSprite;
    }

    public void pickup() {
        if(!this.isPlaced) {
            return;
        }

        this.isPlaced = false;
        var blockData = new TerrainBlockData();
        var blockPosition = new Vector3i();
        for(var blockOffset : Door.doorBlockOffsets) {
            blockPosition.set(this.position).add(blockOffset);
            this.directory.getDoorSystem().removeDoorPosition(blockPosition);
            this.directory.getTerrainSystem().setBlockData(blockPosition, blockData);
        }
    }

    @Override
    public String getStampName() {
        return this.name;
    }

    @Override
    public void applyStamp(
        Vector3i position, 
        Orientation orientation
    ) {
        if(this.isPlaced) {
            return;
        }

        var blockData = new TerrainBlockData();
        var positionBuffer = new Vector3i();

        for(var bottomBlockOffset : Door.doorBlockOffsets) {
            positionBuffer.set(position).sub(bottomBlockOffset);
            this.directory.getTerrainSystem().getBlockData(positionBuffer, blockData);
            if(blockData.blockID != AirBlock.blockID) {
                continue;
            }

            positionBuffer.y += 1;
            this.directory.getTerrainSystem().getBlockData(positionBuffer, blockData);
            if(blockData.blockID != AirBlock.blockID) {
                continue;
            }

            positionBuffer.y -= 1;
            this.orientation = orientation;
            this.position.set(positionBuffer);
            this.isPlaced = true;
        }

        if(!this.isPlaced) {
            return;
        }

        var doorBlock = this.directory.getBlockSystem().doorBlock;
        blockData.set(
            doorBlock.getBlockID(), 
            orientation.orientationID,
            0, 0
        );

        var blockPosition = new Vector3i();
        for(var blockOffset : Door.doorBlockOffsets) {
            blockPosition.set(this.position).add(blockOffset);
            this.directory.getTerrainSystem().setBlockData(blockPosition, blockData);
            this.directory.getDoorSystem().addDoorPosition(blockPosition, this);
        }
    }

    public float getPortalLightLevel() {
        var samplePosition = new Vector3i();
        var blockData = new TerrainBlockData();
        var orientation = this.orientation;

        var portalLight = this.directory.getGlobalConfigSystem().baseLighting;
        for(var blockOffset : Door.doorBlockOffsets) {
            samplePosition.set(this.position).add(blockOffset);
            samplePosition.add(orientation.cubeFace.normal);
            this.directory.getTerrainSystem().getBlockData(samplePosition, blockData);
            var bottomLightLevel = blockData.getLight(LightType.local);
            portalLight = Math.max(portalLight, bottomLightLevel / 15f);
        }

        return portalLight;
    }

    public void open() {
        var doorSystem = this.directory.getDoorSystem();
        if(doorSystem.openDoor == this) {
            return;
        } else if (doorSystem.openDoor != null) {
            doorSystem.openDoor.close();
        }

        doorSystem.openDoor = this;
        var blockData = new TerrainBlockData();
        var blockPosition = new Vector3i();
        for(var blockOffset : Door.doorBlockOffsets) {
            blockPosition.set(this.position).add(blockOffset);
            this.directory.getTerrainSystem().getBlockData(blockPosition, blockData);
            blockData.setLight(LightType.portal, Door.openPortalLightLevel);
            this.directory.getTerrainSystem().setBlockData(blockPosition, blockData);
        }
    }

    public void close() {
        var doorSystem = this.directory.getDoorSystem();
        if(doorSystem.openDoor != this) {
            return;
        }

        doorSystem.openDoor = null;
        var blockData = new TerrainBlockData();
        var blockPosition = new Vector3i();
        for(var blockOffset : Door.doorBlockOffsets) {
            blockPosition.set(this.position).add(blockOffset);
            this.directory.getTerrainSystem().getBlockData(blockPosition, blockData);
            blockData.setLight(LightType.portal, 0);
            this.directory.getTerrainSystem().setBlockData(blockPosition, blockData);
        }
    }

    public Zone resolve() {
        return this.directory
            .getZoneCache()
            .getZone(this.targetZoneName, !this.directory.isShadowCopy());
    }

    @Override
    public String toString() {
        return String.format("Door(%s)", this.name);
    }

}
