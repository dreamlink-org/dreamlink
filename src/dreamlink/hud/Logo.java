package dreamlink.hud;


import org.joml.Vector2i;

import dreamlink.gamestate.home.HomeExploreLogoGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.sprite.template.SolidSpriteTemplate;
import dreamlink.graphics.texture.sample.EntityTextureSample;
import dreamlink.logger.Logger;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector2iMaths;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;

public class Logo implements IHUDComponent {

    private static float rampInStartFactor = 0.2f;
    private static float rampInEndFactor = 0.3f;
    private static float rampOutStartFactor = 0.8f;
    private static float rampOutEndFactor = 0.9f;
    
    public static Logo instance = new Logo();

    private Vector2i position;
    
    public Logo() {
        this.position = new Vector2i(Window.instance.getResolution());
        this.position.sub(EntityTextureSample.logo.dimensions);
        Vector2iMaths.div(this.position, 2);
    }

    public void setup() {
        Logger.instance.debug("Setting up logo HUD element");
        HUDSystem.instance.addHUDComponent(this);
    }

    private float getAlpha() {
        var progress = HomeExploreLogoGameState.instance.getProgress();
        if(progress < rampInStartFactor) {
            return 0f;
        }

        if(progress < rampInEndFactor) {
            return (progress - rampInStartFactor) / (rampInEndFactor - rampInStartFactor);
        }

        if(progress < rampOutStartFactor) {
            return 1f;
        }

        if(progress < rampOutEndFactor) {
            return 1f - (progress - rampOutStartFactor) / (rampOutEndFactor - rampOutStartFactor);
        }

        return 0f;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var gameState = Simulation.instance.getGameState();
        if(gameState != HomeExploreLogoGameState.instance) {
            return;
        }

        SolidSpriteTemplate.white.writeToSpriteBatch(
            spriteBatch,
            Vector2iMaths.zero,
            Window.instance.getResolution(),
            SpriteHeight.background
        );

        spriteBatch.writeTextureSample(
            this.position,
            EntityTextureSample.logo.dimensions,
            SpriteHeight.background,
            EntityTextureSample.logo,
            Vector4fMaths.fromAlpha(this.getAlpha())
        );
    }
    
}
