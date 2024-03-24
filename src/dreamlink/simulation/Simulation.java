package dreamlink.simulation;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL42;

import dreamlink.audio.mixer.SoundMixer;
import dreamlink.audio.mixer.SoundMixerReceiver;
import dreamlink.gamestate.IGameState;
import dreamlink.graphics.framebuffer.DepthBuffer;
import dreamlink.graphics.framebuffer.terrain.OpaqueTerrainFrameBuffer;
import dreamlink.graphics.framebuffer.terrain.PortalFrameBuffer;
import dreamlink.graphics.framebuffer.terrain.TransparentTerrainFrameBuffer;
import dreamlink.graphics.glstate.CullFaceState;
import dreamlink.graphics.glstate.DepthMaskState;
import dreamlink.graphics.glstate.DepthTestState;
import dreamlink.graphics.glstate.FrameBufferState;
import dreamlink.graphics.glstate.PolygonOffsetState;
import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.glstate.blend.BlendState;
import dreamlink.graphics.mesh.CompositeMesh;
import dreamlink.graphics.program.CompositeShaderProgram;
import dreamlink.graphics.program.SpriteShaderProgram;
import dreamlink.graphics.program.TransparentShaderProgram;
import dreamlink.graphics.program.OpaqueShaderProgram;
import dreamlink.graphics.texture.EntityTexture;
import dreamlink.graphics.texture.OpaqueTexture;
import dreamlink.graphics.texture.OverlayTexture;
import dreamlink.graphics.texture.PortalTexture;
import dreamlink.graphics.texture.TransparentAccumulatorTexture;
import dreamlink.graphics.texture.TransparentRevealTexture;
import dreamlink.hud.HUDSystem;
import dreamlink.hud.Logo;
import dreamlink.hud.Reticule;
import dreamlink.hud.ZoneBanner;
import dreamlink.logger.SynchronizedLogRelay;
import dreamlink.menu.MenuSystem;
import dreamlink.menu.component.home.HomeEditLoadZoneComponent;
import dreamlink.menu.component.home.HomeExploreComponent;
import dreamlink.menu.component.home.HomeExploreLoadZoneComponent;
import dreamlink.menu.component.simulation.SimulationExploreComponent;
import dreamlink.menu.component.simulation.SimulationLogViewerComponent;
import dreamlink.menu.component.simulation.SimulationMessageViewerComponent;
import dreamlink.menu.component.simulation.edit.SimulationEditComponent;
import dreamlink.menu.component.simulation.quickbar.QuickBarComponent;
import dreamlink.player.Player;
import dreamlink.utility.maths.PortalTransformer;
import dreamlink.utility.maths.Vector3fMaths;
import dreamlink.utility.worker.WorkerPool;
import dreamlink.window.Window;
import dreamlink.zone.ZoneCache;
import dreamlink.zone.ZoneStatus;
import dreamlink.zone.source.IZoneSourceStrategy;

public class Simulation {

    private static float doorSoundRange = 16f;
    public static long simulationStep = 10;
    public static float updateFactor = Simulation.simulationStep / 1000f;

    public static Simulation instance = new Simulation();

    private long lastUpdateTime;
    private IGameState activeGameState;
    private IGameState nextGameState;

    private SoundMixerReceiver currentReceiver;
    private SoundMixerReceiver portalReceiver;

    public SimulationMode simulationMode;
    public IZoneSourceStrategy zoneSourceStrategy;

    public Simulation() {
        this.currentReceiver = new SoundMixerReceiver();
        this.portalReceiver = new SoundMixerReceiver();
    }

    public void setGameState(IGameState gameState) {
        this.nextGameState = gameState;
    }

    public IGameState getGameState() {
        return this.activeGameState;
    }

    public void setup() {
        // Setup core systems
        Window.instance.setup();
        OpaqueShaderProgram.instance.setup();
        TransparentShaderProgram.instance.setup();
        SpriteShaderProgram.instance.setup();
        CompositeShaderProgram.instance.setup();
        SoundMixer.instance.setup();

        // Initialize global state defaults
        BlendState.initialize();
        DepthTestState.initialize();
        DepthMaskState.initialize();
        FrameBufferState.initialize();
        CullFaceState.initialize();
        PolygonOffsetState.initialize();
        TextureState.initialize();

        // Setup other global systems
        MenuSystem.instance.setup();
        HUDSystem.instance.setup();
        Reticule.instance.setup();
        Logo.instance.setup();
        ZoneBanner.instance.setup();
        SynchronizedLogRelay.instance.setup();

        // Setup shared depth buffer
        DepthBuffer.terrain.setup();
        DepthBuffer.portal.setup();

        // Setup system textures
        OverlayTexture.instance.setup();
        EntityTexture.instance.setup();
        OpaqueTexture.instance.setup();
        TransparentAccumulatorTexture.instance.setup();
        TransparentRevealTexture.instance.setup();
        PortalTexture.instance.setup();

        // Setup render targets
        TransparentTerrainFrameBuffer.instance.setup();
        OpaqueTerrainFrameBuffer.instance.setup();
        PortalFrameBuffer.instance.setup();

        CompositeMesh.instance.setup();

        // Register root menu components with the menu system.
        SimulationEditComponent.instance.setup();
        SimulationMessageViewerComponent.instance.setup();
        SimulationExploreComponent.instance.setup();
        SimulationLogViewerComponent.instance.setup();
        HomeExploreComponent.instance.setup();
        QuickBarComponent.instance.setup();
        HomeEditLoadZoneComponent.instance.setup();
        HomeExploreLoadZoneComponent.instance.setup();
        WorkerPool.instance.setup();

        SoundMixer.instance.setup();
        this.currentReceiver.setup();
        this.portalReceiver.setup();

    }

    private void update() {
        this.activeGameState.update();
        MenuSystem.instance.update();
        SoundMixer.instance.update();
        ZoneCache.instance.update();
        Player.instance.update();
        HomeExploreComponent.instance.update();
        SimulationLogViewerComponent.instance.update();
        QuickBarComponent.instance.update();
        SynchronizedLogRelay.instance.update();

        this.currentReceiver.gainMultiplier = 0f;
        this.portalReceiver.gainMultiplier = 0f;

        var zone = Player.instance.getZone();
        if(zone == null || zone.getZoneStatus() != ZoneStatus.ready) {
            return;
        }

        // Pre-load neighboring zones.
        if(this.simulationMode == SimulationMode.explore) {
            for(var door : zone.getDoors()) {
                door.resolve();
            }
        }

        // Set the current receiver to the player's head position so we can pick
        // up positional sounds of the zone the player is in.
        Player.instance.getHeadPosition(this.currentReceiver.position);
        this.currentReceiver.locationKey = zone != null ? zone.getName() : null;
        this.currentReceiver.gainMultiplier = 1f;

        // If a door is open, position the second receiver at the target door's position.
        // Attenuate the gain based on the player's distance to the corresponding open door.
        var openDoor = zone.getOpenDoor();
        if(openDoor != null) {
            var doorPosition = openDoor.getPlanePosition(new Vector3f());
            var distanceToDoor = this.currentReceiver.position.distance(doorPosition);
            var targetZone = openDoor.resolve();
            var targetDoor = targetZone.getDoorByName(openDoor.targetDoorName);
            targetDoor.getPlanePosition(this.portalReceiver.position);
            this.portalReceiver.locationKey = targetZone.getName();
            if(distanceToDoor < Simulation.doorSoundRange) {
                this.portalReceiver.gainMultiplier = 1f - (distanceToDoor / Simulation.doorSoundRange);
            }
        }
    }

    private void render() {
        GL42.glClear(GL42.GL_COLOR_BUFFER_BIT | GL42.GL_DEPTH_BUFFER_BIT);

        var zone = Player.instance.getZone();
        if(zone != null && zone.getZoneStatus() == ZoneStatus.ready) {
            var headPosition = Player.instance.getHeadPosition(new Vector3f());
            var openDoor = zone.getOpenDoor();

            try (var frameBufferState = new FrameBufferState()) {
                frameBufferState.setState(PortalFrameBuffer.instance);
                GL42.glClear(GL42.GL_DEPTH_BUFFER_BIT | GL42.GL_COLOR_BUFFER_BIT);
                if(openDoor != null) {
                    var targetZone = openDoor.resolve();
                    var targetDoor = targetZone.getDoorByName(openDoor.targetDoorName);

                    var transformer = new PortalTransformer(
                        openDoor.getPlanePosition(new Vector3f()),
                        openDoor.orientation.yaw,
                        targetDoor.getPlanePosition(new Vector3f()),
                        targetDoor.orientation.yaw
                    );

                    var transformedPosition = transformer.getTransformedPosition(
                        new Vector3f(headPosition)
                    );

                    var transformedRotation = transformer.getTransformedRotation(
                        new Vector3f(Player.instance.getRotation())
                    );

                    targetZone.render(
                        transformedPosition, 
                        transformedRotation,
                        targetDoor.getPlanePosition(new Vector3f()),
                        new Vector3f(targetDoor.orientation.cubeFace.normal),
                        Simulation.instance.simulationMode == SimulationMode.edit
                    );
                }
            }

            zone.render(
                headPosition, 
                Player.instance.getRotation(),
                Vector3fMaths.zero,
                Vector3fMaths.zero,
                Simulation.instance.simulationMode == SimulationMode.edit
            );
        }

        MenuSystem.instance.render();
        HUDSystem.instance.render();
    }

    public void destroy() {
        WorkerPool.instance.destroy();
    }
    
    public void run() {
        while(!Window.instance.shouldClose()) {
            var updateStartTime = System.currentTimeMillis();
            if(this.lastUpdateTime == 0) {
                this.lastUpdateTime = updateStartTime;
            }

            while(this.lastUpdateTime < updateStartTime) {
                if(this.nextGameState != null) {
                    this.activeGameState = this.nextGameState;
                    this.activeGameState.onBind();
                    this.nextGameState = null;
                }

                Window.instance.handleUserInputs();
                this.update();
                this.lastUpdateTime += Simulation.simulationStep;
            }

            this.render();
            Window.instance.swapBuffers();
        }
    }

}
