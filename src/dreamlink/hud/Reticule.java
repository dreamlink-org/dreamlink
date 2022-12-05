package dreamlink.hud;


import org.joml.Vector2i;

import dreamlink.Config;
import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;

public class Reticule implements IHUDComponent {

    public static Reticule instance = new Reticule();

    public void setup() {
        HUDSystem.instance.addHUDComponent(this);
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var gameState = Simulation.instance.getGameState();
        if(gameState != SimulationGameState.instance) {
            return;
        }

        var reticuleSprite = Player.instance.canInteract()
            ? OverlayTextureSample.reticuleInteraction
            : OverlayTextureSample.reticule;

        var reticulePosition = new Vector2i(
            (Config.instance.resolution.x - reticuleSprite.dimensions.x) / 2,
            (Config.instance.resolution.y - reticuleSprite.dimensions.y) / 2
        );

        reticuleSprite.writeToSpriteBatch(
            spriteBatch,
            reticulePosition,
            reticuleSprite.dimensions,
            SpriteHeight.hud
        );
    }
    
}
