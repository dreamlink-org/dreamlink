package dreamlink.hud;


import org.joml.Vector2i;

import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.logger.Logger;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;

public class Reticule implements IHUDComponent {

    public static Reticule instance = new Reticule();

    public boolean showReticule;

    public Reticule() {
        this.showReticule = true;
    }

    public void setup() {
        Logger.instance.debug("Setting up reticule HUD element");
        HUDSystem.instance.addHUDComponent(this);
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var gameState = Simulation.instance.getGameState();
        if(gameState != SimulationGameState.instance || !this.showReticule) {
            return;
        }

        var reticuleSprite = Player.instance.canInteract()
            ? OverlayTextureSample.reticuleInteraction
            : OverlayTextureSample.reticule;

        var reticulePosition = new Vector2i(
            (Window.instance.getResolution().x - reticuleSprite.dimensions.x) / 2,
            (Window.instance.getResolution().y - reticuleSprite.dimensions.y) / 2
        );

        reticuleSprite.writeToSpriteBatch(
            spriteBatch,
            reticulePosition,
            reticuleSprite.dimensions,
            SpriteHeight.hud
        );
    }
    
}
