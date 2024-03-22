package dreamlink.zone;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2i;

import dreamlink.graphics.texture.Texture;
import dreamlink.graphics.texture.data.RGBA8ImageTextureData;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.graphics.texture.sample.ZoneTextureSample;
import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.utility.JSONFns;
import dreamlink.utility.maths.FloatMaths;
import dreamlink.utility.maths.IntMaths;
import dreamlink.utility.maths.Vector2iMaths;

public class ZoneTextureSystem {

    private static int maxAnimationSize = 0xFF;
    private static int maxAnimationSpeedSize = 0xF;

    private static Path atlasPath = Paths.get("texture/atlas.png");
    private static Path samplesDirectory = Paths.get("texture/samples");
    private static String sampleSuffix = ".json";
    
    private Map<String, TextureSample> textureSampleLookup;
    private boolean isSetup;

    public Texture texture;
    public RGBA8ImageTextureData textureData;

    public ZoneTextureSystem(IZoneDirectory directory) {
        this.textureSampleLookup = new HashMap<>();
        this.texture = new Texture();
    }

    public TextureSample getTextureSample(String name) {
        return this.textureSampleLookup.get(name);
    }

    public void loadData(Path zonePath) {
        var textureFile = zonePath
            .resolve(ZoneTextureSystem.atlasPath)
            .toFile();

        Logger.instance.debug(String.format("Loading texture data from: %s", textureFile.getName()));
        this.textureData = RGBA8ImageTextureData.fromFile(textureFile);

        var samplesDirectoryFile = zonePath
            .resolve(ZoneTextureSystem.samplesDirectory)
            .toFile();

        for(var sampleFile : samplesDirectoryFile.listFiles(File::isFile)) {
            var fileName = sampleFile.getName(); 
            if(!fileName.endsWith(ZoneTextureSystem.sampleSuffix)) {
                continue;
            }
            var sampleName = fileName.substring(0, fileName.length() - ZoneTextureSystem.sampleSuffix.length());

            Logger.instance.debug(String.format("Loading texture sample config: %s", fileName));
            var sampleConfig = FileFns.readJSONFromFile(sampleFile);
            var positionJSON = sampleConfig.getJSONArray("lookup.position");
            var position = JSONFns.getVector2iFromJSON(new Vector2i(), positionJSON);

            var dimensionsJSON = sampleConfig.getJSONArray("lookup.dimensions");
            var dimensions = JSONFns.getVector2iFromJSON(new Vector2i(), dimensionsJSON);

            var strideJSON = sampleConfig.optJSONArray("animation.stride");
            var stride = strideJSON != null 
                ? JSONFns.getVector2iFromJSON(new Vector2i(), strideJSON) 
                : Vector2iMaths.zero;

            var animationSpeed = FloatMaths.clamp(sampleConfig.optFloat("animation.speed", 0.5f), 0f, 1f);
            var quantizedSpeed = (int)(animationSpeed * ZoneTextureSystem.maxAnimationSpeedSize);
            var textureSample = new ZoneTextureSample(
                this.textureData.dimensions,
                position,
                dimensions,
                stride,
                IntMaths.clamp(
                    sampleConfig.optInt("animation.frames.total", 1) - 1, 
                    0, 
                    ZoneTextureSystem.maxAnimationSize
                ),
                IntMaths.clamp(
                    sampleConfig.optInt("animation.frames.row", 1) - 1,
                    0,
                    ZoneTextureSystem.maxAnimationSize
                ),
                IntMaths.clamp(
                    sampleConfig.optInt("animation.frames.start", 0),
                    0,
                    ZoneTextureSystem.maxAnimationSize
                ),
                quantizedSpeed
            );

            this.textureSampleLookup.put(sampleName, textureSample);
        }
    }

    public void setup() {
        this.isSetup = true;
        this.texture.setup();
        this.texture.bufferData(this.textureData);
    }

    public void destroy() {
        if(this.isSetup) {
            this.texture.destroy();
        }
    }
}
