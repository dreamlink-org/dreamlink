package dreamlink.hud;

import org.joml.Vector2i;
import org.joml.Vector4f;

import dreamlink.Config;
import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.graphics.text.CharacterTextureSampleLookup;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.zone.ZoneStatus;

public class ZoneBanner implements IHUDComponent {

    private static Vector4f backgroundColor = new Vector4f(0, 0, 0, 0.5f);

    public static ZoneBanner instance = new ZoneBanner();

    private int maxCharacters;

    public ZoneBanner() {
        this.maxCharacters = Config.instance.resolution.x / OverlayTextureSample.glyphDimensions.x;
    }

    public void setup() {
        HUDSystem.instance.addHUDComponent(this);
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(Simulation.instance.getGameState() != SimulationGameState.instance) {
            return;
        }

        var currentZone = Player.instance.getZone();
        if(currentZone.getZoneStatus() != ZoneStatus.ready) {
            return;
        }

        var zoneTitle = currentZone.getName();

        if(zoneTitle.length() > this.maxCharacters) {
            zoneTitle = zoneTitle.substring(0, this.maxCharacters);
        }

        var bannerWidth = zoneTitle.length() * OverlayTextureSample.glyphDimensions.x;
        var bannerPosition = new Vector2i((Config.instance.resolution.x - bannerWidth) / 2, 0);

        var characterPosition = new Vector2i(bannerPosition);
        for(var ix = 0; ix < zoneTitle.length(); ix += 1) {
            var glyph = CharacterTextureSampleLookup.instance.getTextureSample(
                zoneTitle.charAt(ix),
                FontDecoration.underline
            );

            spriteBatch.writeTextureSample(
                characterPosition,
                OverlayTextureSample.glyphDimensions,
                SpriteHeight.hud,
                OverlayTextureSample.white,
                ZoneBanner.backgroundColor
            );

            spriteBatch.writeTextureSample(
                characterPosition,
                OverlayTextureSample.glyphDimensions,
                SpriteHeight.hud,
                glyph,
                Vector4fMaths.white
            );

            characterPosition.x += OverlayTextureSample.glyphDimensions.x;
        }
    }
    
}
