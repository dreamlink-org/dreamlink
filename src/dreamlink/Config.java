package dreamlink;

import java.net.URI;

import org.joml.Vector2i;

import dreamlink.window.button.Button;
import dreamlink.utility.CommonPaths;
import dreamlink.utility.FileFns;

public class Config {

    private static int defaultNumThreads = 2;
    private static int defaultLevelCacheSize = 16;
    private static int minimumLevelCacheSize = 16;
    private static float defaultMouseSensitivity = 40f;
    private static int defaultMaxAudioSources = 16;
    private static String defaultNexusRoot = "https://dreamlink.tlonny.io/nexus";

    public static Config instance = new Config();

    public Vector2i resolution;
    public int numThreads;
    public int levelCacheSize;
    public Button placeBlockButton;
    public Button eraseBlockButton;
    public Button interactButton;
    public Button editButton;
    public Button mainMenuButton;
    public float mouseSensitivity;
    public boolean borderlessWindowedMode;
    public URI nexusRoot;
    public String nexusDreamCode;
    public int maxAudioSources;

    public Config() {
        var configFile = CommonPaths.instance.configPath.resolve("config.json").toFile();
        var config = FileFns.readJSONFromFile(configFile);

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
