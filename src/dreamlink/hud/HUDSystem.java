package dreamlink.hud;

import java.util.ArrayList;
import java.util.List;

import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.glstate.blend.BlendMode;
import dreamlink.graphics.glstate.blend.BlendState;
import dreamlink.graphics.mesh.strategy.texture.MeshTextureQuadStrategy;
import dreamlink.graphics.program.SpriteShaderProgram;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.texture.EntityTexture;
import dreamlink.graphics.texture.OverlayTexture;
import dreamlink.graphics.texture.Texture;
import dreamlink.player.Player;
import dreamlink.zone.ZoneStatus;

public class HUDSystem {

    public static int reticuleHeight = 0;

    public static HUDSystem instance = new HUDSystem();

    private List<IHUDComponent> hudComponents;
    private SpriteBatch spriteBatch = new SpriteBatch();

    public HUDSystem() {
        this.hudComponents = new ArrayList<>();
    }

    public void setup() {
        this.spriteBatch.setup();
    }

    public void addHUDComponent(IHUDComponent hudComponent) {
        this.hudComponents.add(hudComponent);
    }

    private Texture getZoneTexture() {
        var zone = Player.instance.getZone();
        return zone == null || zone.getZoneStatus() != ZoneStatus.ready 
            ? null 
            : zone.getTexture();
    }

    public void render() {
        this.spriteBatch.clear();
        for(var hudComponent : this.hudComponents) {
            hudComponent.writeToSpriteBatch(this.spriteBatch);
        }

        try(
            var blend = new BlendState();
            var zoneTextureState = new TextureState(SpriteShaderProgram.zoneTextureUnitID);
            var entityTextureState = new TextureState(SpriteShaderProgram.entityTextureUnitID);
            var overlayTextureState = new TextureState(SpriteShaderProgram.overlayTextureUnitID);
        ) {
            blend.setState(BlendMode.alphaBlend);
            zoneTextureState.setState(this.getZoneTexture());
            entityTextureState.setState(EntityTexture.instance);
            overlayTextureState.setState(OverlayTexture.instance);

            SpriteShaderProgram.instance.bind();

            int currentFrame = MeshTextureQuadStrategy.getAnimationFrame();
            SpriteShaderProgram.instance.setAnimationFrame(currentFrame);

            this.spriteBatch.render();
        }


    }
    
}
