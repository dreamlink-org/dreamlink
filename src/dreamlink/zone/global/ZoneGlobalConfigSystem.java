package dreamlink.zone.global;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.joml.Vector3i;

import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.utility.JSONFns;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.Vector3iMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.ZoneLoadException;

public class ZoneGlobalConfigSystem {

    private static int maxChunkSpaceVolume = 1024;
    private static Path skyBoxPath = Paths.get("zone.json");

    private IZoneDirectory directory;
    private SkyBox skyBox;
    private boolean isSetup;

    public Vector3i chunkSpaceDimensions;
    public float baseLighting;

    public ZoneGlobalConfigSystem(IZoneDirectory directory) {
        this.directory = directory;
        this.skyBox = new SkyBox();
        this.chunkSpaceDimensions = new Vector3i();
    }

    public boolean isAffectedByLight() {
        return this.skyBox.isAffectedByLight;
    }

    public void isAffectedByLight(boolean isAffectedByLight) {
        this.skyBox.isAffectedByLight = isAffectedByLight;
    }

    public void loadData(Path zonePath) {
        var configFile = zonePath
            .resolve(ZoneGlobalConfigSystem.skyBoxPath)
            .toFile();

        Logger.instance.debug(String.format("Loading global config: %s", configFile.getName()));
        var config = FileFns.readJSONFromFile(configFile);

        var chunkSpaceDimensionsArray = config.getJSONArray("terrain.dimensions.chunk_space");
        JSONFns.getVector3iFromJSON(this.chunkSpaceDimensions, chunkSpaceDimensionsArray);
        var volume = Vector3iMaths.getVolume(this.chunkSpaceDimensions);
        if(volume > ZoneGlobalConfigSystem.maxChunkSpaceVolume) {
            var msg = String.format("Zone chunk space volume exceeds maximum: %d", volume);
            throw new ZoneLoadException(msg);
        }

        this.baseLighting = config.optFloat("lighting.base", 1f);
        this.skyBox.isAffectedByLight = config.optBoolean("skybox.lighting.is_affected", false);
        for(var cubeFace : CubeFace.getCubeFaces()) {
            var textureSampleKey = String.format("skybox.texture.sample.%s", cubeFace.name);
            var textureName = config.optString(textureSampleKey, null);
            var textureSample = this.directory.getTextureSystem().getTextureSample(textureName);
            if(textureSample == null && textureName != null) {
                var msg = String.format("Missing %s skybox texture sample: %s", cubeFace.name, textureName);
                Logger.instance.warn(msg);
                continue;
            }

            this.skyBox.setTextureEntryName(cubeFace, textureSample);
        }
    }

    public void setup() {
        this.isSetup = true;
        this.skyBox.setup();
        this.skyBox.buildMesh();
    }

    public void render() {
        this.skyBox.render();
    }

    public void destroy() {
        if(this.isSetup) {
            this.skyBox.destroy();
        }
    }
}
