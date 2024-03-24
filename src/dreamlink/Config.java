package dreamlink;

import java.net.URI;

import org.json.JSONObject;

import dreamlink.utility.CommonPaths;
import dreamlink.utility.FileFns;

public class Config {

    private static int defaultNumThreads = 2;
    private static int defaultLevelCacheSize = 16;
    private static int minimumLevelCacheSize = 16;
    private static float defaultMouseSensitivity = 40f;
    private static int defaultMaxAudioSources = 16;
    private static String defaultNexusRoot = "https://dreamlink.lonny.io/nexus";

    public static Config instance = new Config();

    public int numThreads;
    public int levelCacheSize;
    public float mouseSensitivity;
    public boolean borderlessWindowedMode;
    public URI nexusRoot;
    public String nexusDreamCode;
    public int maxAudioSources;

    public Config() {
        var configFile = CommonPaths.instance.configPath.resolve("config.json").toFile();
        var config = configFile.exists()
            ? FileFns.readJSONFromFile(configFile)
            : new JSONObject();

        this.nexusRoot = URI.create(config.optString("nexus.root", Config.defaultNexusRoot));
        this.nexusDreamCode = config.optString("nexus.dreamcode").replace("-", "");
        this.borderlessWindowedMode = config.optBoolean("display.borderless_windowed_mode", false);

        this.numThreads = config.optInt(
            "system.num_threads", 
            Config.defaultNumThreads
        );

        this.levelCacheSize = Math.max(
            config.optInt(
                "system.level_cache_size", 
                Config.defaultLevelCacheSize
            ),
            Config.minimumLevelCacheSize
        );

        this.mouseSensitivity = config.optFloat(
            "input.mouse_sensitivity", 
            defaultMouseSensitivity
        );
        this.maxAudioSources = config.optInt(
            "audio.max_sources", 
            Config.defaultMaxAudioSources
        );
    }

}
