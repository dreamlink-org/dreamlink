package dreamlink.zone;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.json.JSONException;
import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

import dreamlink.graphics.framebuffer.terrain.OpaqueTerrainFrameBuffer;
import dreamlink.graphics.framebuffer.terrain.TransparentTerrainFrameBuffer;
import dreamlink.graphics.glstate.CullFaceState;
import dreamlink.graphics.glstate.DepthMaskState;
import dreamlink.graphics.glstate.DepthTestState;
import dreamlink.graphics.glstate.FrameBufferState;
import dreamlink.graphics.glstate.PolygonOffsetState;
import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.glstate.blend.BlendMode;
import dreamlink.graphics.glstate.blend.BlendState;
import dreamlink.graphics.mesh.CompositeMesh;
import dreamlink.graphics.mesh.strategy.texture.MeshTextureQuadStrategy;
import dreamlink.graphics.program.CompositeShaderProgram;
import dreamlink.graphics.program.OpaqueShaderProgram;
import dreamlink.graphics.program.TransparentShaderProgram;
import dreamlink.graphics.texture.EntityTexture;
import dreamlink.graphics.texture.OpaqueTexture;
import dreamlink.graphics.texture.PortalTexture;
import dreamlink.graphics.texture.Texture;
import dreamlink.graphics.texture.TransparentAccumulatorTexture;
import dreamlink.graphics.texture.TransparentRevealTexture;
import dreamlink.logger.Logger;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.utility.worker.WorkerPool;
import dreamlink.utility.worker.WorkerTask;
import dreamlink.zone.block.IBlock;
import dreamlink.zone.block.UserBlock;
import dreamlink.zone.block.ZoneBlockSystem;
import dreamlink.zone.door.Door;
import dreamlink.zone.door.ZoneDoorSystem;
import dreamlink.zone.global.ZoneGlobalConfigSystem;
import dreamlink.zone.speaker.Speaker;
import dreamlink.zone.speaker.ZoneSpeakerSystem;
import dreamlink.zone.terrain.TerrainBlockData;
import dreamlink.zone.terrain.ZoneTerrainSystem;

public class Zone {

    private class InternalZoneDirectory implements IZoneDirectory {

        @Override
        public ZoneCache getZoneCache() {
            return Zone.this.zoneCache;
        }

        @Override
        public ZoneTextureSystem getTextureSystem() {
            return Zone.this.textureSystem;
        }

        @Override
        public ZoneTerrainSystem getTerrainSystem() {
            return Zone.this.terrainSystem;
        }

        @Override
        public ZoneDoorSystem getDoorSystem() {
            return Zone.this.doorSystem;
        }

        @Override
        public ZoneSpeakerSystem getSpeakerSystem() {
            return Zone.this.speakerSystem;
        }

        @Override
        public ZoneGlobalConfigSystem getGlobalConfigSystem() {
            return Zone.this.globalConfigModule;
        }

        @Override
        public ZoneBlockSystem getBlockSystem() {
            return Zone.this.blockSystem;
        }

        @Override
        public boolean isShadowCopy() {
            return Zone.this.isShadowCopy;
        }

        @Override
        public String getName() {
            return Zone.this.name;
        }

        @Override
        public ZoneSoundSystem getSoundSystem() {
            return Zone.this.soundSystem;
        }

    }

    private static FloatBuffer clearBuffer = MemoryUtil.memAllocFloat(4);

    private ZoneStatus zoneStatus;
    private WorkerTask loadTask;

    private ZoneTextureSystem textureSystem;
    private ZoneGlobalConfigSystem globalConfigModule;
    private ZoneBlockSystem blockSystem;
    private ZoneDoorSystem doorSystem;
    private ZoneTerrainSystem terrainSystem;
    private ZoneSoundSystem soundSystem;
    private ZoneSpeakerSystem speakerSystem;
    private ZoneCache zoneCache;

    public String name;
    public boolean isShadowCopy;

    public Zone(ZoneCache zoneCache, String zoneKey, boolean isShadowCopy) {
        this.zoneCache = zoneCache;
        this.zoneStatus = ZoneStatus.fetching;
        this.name = zoneKey;
        this.isShadowCopy = isShadowCopy;

        var zoneDirectory = new InternalZoneDirectory();
        this.textureSystem = new ZoneTextureSystem(zoneDirectory);
        this.soundSystem = new ZoneSoundSystem();
        this.globalConfigModule = new ZoneGlobalConfigSystem(zoneDirectory);
        this.blockSystem = new ZoneBlockSystem(zoneDirectory);
        this.doorSystem = new ZoneDoorSystem(zoneDirectory);
        this.terrainSystem = new ZoneTerrainSystem(zoneDirectory);
        this.speakerSystem = new ZoneSpeakerSystem(zoneDirectory);
    }

    public Texture getTexture() {
        return this.textureSystem.texture;
    }

    public String getName() {
        return this.name;
    }

    public Vector3i getDimensions() {
        return this.terrainSystem.dimensions;
    }

    public int getSpeakerCount() {
        return this.speakerSystem.getSpeakerCount();
    }

    public Speaker getSpeakerByIndex(int index) {
        return this.speakerSystem.getSpeakerByIndex(index);
    }

    public Speaker getSpeakerByPosition(Vector3i position) {
        return this.speakerSystem.getSpeakerByPosition(position);
    }

    public TerrainBlockData getBlockData(Vector3i position, TerrainBlockData blockData) {
        return this.terrainSystem.getBlockData(position, blockData);
    }

    public void setBlockData(Vector3i position, TerrainBlockData blockData) {
        this.terrainSystem.setBlockData(position, blockData);
    }

    public IBlock getBlockByID(int blockID) {
        return this.blockSystem.getBlockByID(blockID);
    }

    public int getUserBlockCount() {
        return this.blockSystem.getUserBlockCount();
    }

    public UserBlock getUserBlockByIndex(int index) {
        return this.blockSystem.getUserBlockByIndex(index);
    }

    public Door getDoorByIndex(int index) {
        return this.doorSystem.getDoorByIndex(index);
    }

    public Iterable<Door> getDoors() {
        return this.doorSystem.getDoors();
    }

    public Door getDoorByName(String doorName) {
        return this.doorSystem.getDoorByName(doorName);
    }

    public Door getDoorByPosition(Vector3i position) {
        return this.doorSystem.getDoorByPosition(position);
    }

    public Door getOpenDoor() {
        return this.doorSystem.openDoor;
    }

    public int getDoorCount() {
        return this.doorSystem.getDoorCount();
    }

    public ZoneStatus getZoneStatus() {
        return this.zoneStatus;
    }

    public boolean isReady() {
        return this.zoneStatus == ZoneStatus.ready;
    }

    public void loadData() {
        try (var zoneData = Simulation.instance.zoneSourceStrategy.fetchZoneData(this.name)) {
            var zonePath = zoneData.getPath();

            this.textureSystem.loadData(zonePath);
            this.soundSystem.loadData(zonePath);
            this.globalConfigModule.loadData(zonePath);
            this.blockSystem.loadData(zonePath);
            this.doorSystem.loadData(zonePath);
            this.speakerSystem.loadData(zonePath);
            this.terrainSystem.loadData(zonePath);
        } catch (JSONException e) {
            Logger.instance.error(String.format("Failed to load JSON: %s", e.getMessage()));
            throw e;
        } catch (RuntimeException e) {
            Logger.instance.error(e.getMessage());
            throw e;
        }
    }

    public void saveZone() {
        try (var zoneData = Simulation.instance.zoneSourceStrategy.fetchZoneData(this.name)) {
            var zonePath = zoneData.getPath();
            this.terrainSystem.saveData(zonePath);
            this.doorSystem.saveData(zonePath);
            this.speakerSystem.saveData(zonePath);
        }
    }

    public void update() {
        // If the zone failed to load, then there is no work to be done.
        if(this.zoneStatus == ZoneStatus.failed) {
            return;
        
        // If the zone is waiting to be loaded, then we should start the load..
        } else if(this.zoneStatus == ZoneStatus.fetching) {
            this.zoneStatus = ZoneStatus.loading;
            this.loadTask = WorkerPool.instance.submit(this::loadData);
        // Again, lets poll the load task until its done, allowing us to move
        // onto the step: finalizing (setting up the GL resources, which have
        // to happen on the main thread, not a worker thread).
        } else if(this.zoneStatus == ZoneStatus.loading) {
            if(!this.loadTask.isDone()) {
                return;
            }

            try {
                this.loadTask.join();
                Logger.instance.debug("Zone successfully loaded.");
            } catch (Exception e) {
                this.zoneStatus = ZoneStatus.failed;
                return;
            }

            this.globalConfigModule.setup();
            this.textureSystem.setup();
            this.soundSystem.setup();
            this.zoneStatus = ZoneStatus.finalizing;
        } else {
            if(!this.terrainSystem.updateDirtyChunks()) {
                this.zoneStatus = ZoneStatus.ready;
            }
        }
    }

    public void destroy() {
        Logger.instance.debug(String.format("Destroying zone: %s", this.name));
        this.globalConfigModule.destroy();
        this.speakerSystem.destroy();
        this.soundSystem.destroy();
        this.textureSystem.destroy();
        this.terrainSystem.destroy();
    }

    public void render(
        Vector3f cameraPosition, 
        Vector3f cameraRotation,
        Vector3f clipPosition,
        Vector3f clipNormal,
        boolean isShowHidden
    ) {
        var portalLight = 0f;
        if(this.doorSystem.openDoor != null) {
            var targetZone = this.zoneCache.getZone(
                this.doorSystem.openDoor.targetZoneName, 
                !this.isShadowCopy
            );
            var targetDoor = targetZone.getDoorByName(this.doorSystem.openDoor.targetDoorName);
            portalLight = targetDoor.getPortalLightLevel();
        }
        
        var currentAnimationFrame = MeshTextureQuadStrategy.getAnimationFrame();

        try(
            var zoneTextureState = new TextureState(OpaqueShaderProgram.zoneTextureUnitID);
            var entityTextureState = new TextureState(OpaqueShaderProgram.entityTextureUnitID);
            var portalTextureState = new TextureState(OpaqueShaderProgram.portalTextureUnitID);
            var frameBufferState = new FrameBufferState();
            var depthTest = new DepthTestState();
            var cullFace = new CullFaceState();
        ) {
            zoneTextureState.setState(this.textureSystem.texture);
            entityTextureState.setState(EntityTexture.instance);
            portalTextureState.setState(PortalTexture.instance);
            depthTest.setState(true);
            cullFace.setState(true);
            frameBufferState.setState(OpaqueTerrainFrameBuffer.instance);

            OpaqueShaderProgram.instance.bind();
            OpaqueShaderProgram.instance.setBaseLight(this.globalConfigModule.baseLighting);
            OpaqueShaderProgram.instance.setPortalLight(portalLight);
            OpaqueShaderProgram.instance.setModel(Vector3fMaths.zero);
            OpaqueShaderProgram.instance.setClip(clipPosition, clipNormal);
            OpaqueShaderProgram.instance.setCamera(cameraPosition, cameraRotation);
            OpaqueShaderProgram.instance.isShowHidden(isShowHidden);
            OpaqueShaderProgram.instance.setAnimationFrame(currentAnimationFrame);

            GL42.glClear(GL42.GL_DEPTH_BUFFER_BIT | GL42.GL_COLOR_BUFFER_BIT);
            this.terrainSystem.renderOpaque();

            OpaqueShaderProgram.instance.setModel(cameraPosition);
            cullFace.setState(false);
            this.globalConfigModule.render();

        }

        try(
            var zoneTextureState = new TextureState(TransparentShaderProgram.zoneTextureUnitID);
            var entityTextureState = new TextureState(TransparentShaderProgram.entityTextureUnitID);
            var frameBufferState = new FrameBufferState();
            var accumulatorBlend = new BlendState(TransparentTerrainFrameBuffer.accumulatorAttachmentID);
            var revealBlend = new BlendState(TransparentTerrainFrameBuffer.revealAttachmentID);
            var depthMask = new DepthMaskState();
            var depthTest = new DepthTestState();
            var polygonOffset = new PolygonOffsetState();
        ) {
            var revealBlendMode = new BlendMode(GL42.GL_ZERO, GL42.GL_ONE_MINUS_SRC_COLOR);
            accumulatorBlend.setState(BlendMode.accumulate);
            revealBlend.setState(revealBlendMode);
            frameBufferState.setState(TransparentTerrainFrameBuffer.instance);
            depthMask.setState(false);
            depthTest.setState(true);
            polygonOffset.setState(-1f, -1f);
            zoneTextureState.setState(this.textureSystem.texture);
            entityTextureState.setState(EntityTexture.instance);
            
            TransparentShaderProgram.instance.bind();
            TransparentShaderProgram.instance.setBaseLight(this.globalConfigModule.baseLighting);
            TransparentShaderProgram.instance.setPortalLight(portalLight);
            TransparentShaderProgram.instance.setModel(Vector3fMaths.zero);
            TransparentShaderProgram.instance.setClip(clipPosition, clipNormal);
            TransparentShaderProgram.instance.setCamera(cameraPosition, cameraRotation);
            TransparentShaderProgram.instance.isShowHidden(isShowHidden);
            TransparentShaderProgram.instance.setAnimationFrame(currentAnimationFrame);
    
            Vector4fMaths.zero.get(Zone.clearBuffer);
            GL42.glClearBufferfv(GL42.GL_COLOR, TransparentTerrainFrameBuffer.accumulatorAttachmentID, Zone.clearBuffer);

            Vector4fMaths.one.get(clearBuffer);
            GL42.glClearBufferfv(GL42.GL_COLOR, TransparentTerrainFrameBuffer.revealAttachmentID, Zone.clearBuffer);
            
            this.terrainSystem.renderTransparent();
        }

        try(
            var opaqueTextureState = new TextureState(CompositeShaderProgram.opaqueTextureUnitID);
            var transparentAccumulatorTextureState = new TextureState(CompositeShaderProgram.transparentAccumulatorTextureUnitID);
            var transparentRevealTextureState = new TextureState(CompositeShaderProgram.transparentRevealTextureUnitID);
        ) {
            opaqueTextureState.setState(OpaqueTexture.instance);
            transparentAccumulatorTextureState.setState(TransparentAccumulatorTexture.instance);
            transparentRevealTextureState.setState(TransparentRevealTexture.instance);
            CompositeShaderProgram.instance.bind();
            CompositeMesh.instance.render();
        }
    }

    @Override
    public String toString() {
        return String.format("Zone(%s.%s)", this.name, this.isShadowCopy);
    }
}