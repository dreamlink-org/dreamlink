package doors;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL42;

import doors.core.graphics.mesh.MeshBuffer;
import doors.core.graphics.rendertarget.PhysicalRenderTarget;
import doors.core.graphics.rendertarget.VirtualRenderTarget;
import doors.core.graphics.texture.ImageTexture;
import doors.core.graphics.texture.TextureChannel;
import doors.core.utility.vector.Vector2in;
import doors.graphics.entity.DoorMesh;
import doors.graphics.entity.PortalMesh;
import doors.state.MainMenuExploreGameState;
import doors.state.MainMenuRootGameState;
import doors.state.explore.ExploreGameState;
import doors.core.graphics.Shader;
import doors.core.Game;
import doors.state.GameState;

public class Doors extends Game {

    public static TextureChannel TEXTURE_CHANNEL_FONT = TextureChannel.TEXTURE_CHANNEL_0;
    public static TextureChannel TEXTURE_CHANNEL_UI = TextureChannel.TEXTURE_CHANNEL_1;
    public static TextureChannel TEXTURE_CHANNEL_ENTITY = TextureChannel.TEXTURE_CHANNEL_2;
    public static TextureChannel TEXTURE_CHANNEL_BLOCK = TextureChannel.TEXTURE_CHANNEL_3;
    public static TextureChannel TEXTURE_CHANNEL_CURRENT_RENDER = TextureChannel.TEXTURE_CHANNEL_4;
    public static TextureChannel TEXTURE_CHANNEL_PORTAL_RENDER = TextureChannel.TEXTURE_CHANNEL_5;

    public static ImageTexture TEXTURE_FONT = new ImageTexture(
        TEXTURE_CHANNEL_FONT, 
        new Vector2in(256, 96),
        "data/texture/font/standard.png"
    );
    public static ImageTexture TEXTURE_UI = new ImageTexture(
        TEXTURE_CHANNEL_UI,
        new Vector2in(256, 256),
        "data/texture/ui.png"
    );

    public static ImageTexture TEXTURE_ENTITY = new ImageTexture(
        TEXTURE_CHANNEL_ENTITY,
        new Vector2in(512, 512),
        "data/texture/entity.png"
    );

    public static VirtualRenderTarget RENDER_TARGET_CURRENT = new VirtualRenderTarget(TEXTURE_CHANNEL_CURRENT_RENDER);
    public static VirtualRenderTarget RENDER_TARGET_PORTAL = new VirtualRenderTarget(TEXTURE_CHANNEL_PORTAL_RENDER);
    public static MeshBuffer SPRITE_BATCH = new MeshBuffer(5_000);

    @Override
    protected void setup() {
        super.setup();
        
        Screen.SCREEN.setup();

        TEXTURE_FONT.setup();
        TEXTURE_FONT.useTexture();

        TEXTURE_UI.setup();
        TEXTURE_UI.useTexture();

        TEXTURE_ENTITY.setup();
        TEXTURE_ENTITY.useTexture();

        RENDER_TARGET_CURRENT.setup();
        RENDER_TARGET_CURRENT.texture.useTexture();

        RENDER_TARGET_PORTAL.setup();
        RENDER_TARGET_PORTAL.texture.useTexture();

        DoorMesh.DOOR_MESH.setup();
        PortalMesh.PORTAL_MESH.setup();

        MainMenuRootGameState.MAIN_MENU_ROOT_GAME_STATE.setup();
        MainMenuExploreGameState.MAIN_MENU_EXPLORE_GAME_STATE.setup();

        MainMenuRootGameState.MAIN_MENU_ROOT_GAME_STATE.use();
        //ExploreGameState.EXPLORE_GAME_STATE.use("scratch/sphere_2");
    }

    @Override
    protected void update() {
        super.update();

        GameState.USED_GAME_STATE.update();
        ExitListener.EXIT_LISTENER.update();

        PhysicalRenderTarget.PHYSICAL_RENDER_TARGET.useRenderTarget();
        GL42.glDisable(GL42.GL_DEPTH_TEST);
        GL42.glClear(GL42.GL_COLOR_BUFFER_BIT | GL42.GL_DEPTH_BUFFER_BIT);
        Shader.SHADER.setFlatViewMatrices();
        Screen.SCREEN.flush();
    }

    public static void main(String[] args) {
        new Doors().run();
    }

}