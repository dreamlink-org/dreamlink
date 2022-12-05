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
    private static Button defaultPlaceBlockButton = Button.mouseLeft;
    private static Button defaultEraseBlockButton = Button.mouseRight;
    private static Button defaultInteractButon = Button.keyE;
    private static Button defaultEditButton = Button.keyTab;
    private static int defaultMaxAudioSources = 16;
    private static Button defaultMainMenuButton = Button.keyEscape;
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
    public URI nexusRoot;
    public String nexusDreamCode;
    public int maxAudioSources;

    public Config() {
        var configFile = CommonPaths.instance.configPath.resolve("config.json").toFile();
        var config = FileFns.readJSONFromFile(configFile);

        var resolutionArray = config.getJSONArray("display.resolution");
        this.resolution = new Vector2i(
            resolutionArray.getInt(0),
            resolutionArray.getInt(1)
        );

        this.nexusRoot = URI.create(config.optString("nexus.root", Config.defaultNexusRoot));
        this.nexusDreamCode = config.optString("nexus.dreamcode").replace("-", "");

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

        this.mainMenuButton = Button.getButton(config.optString(
            "input.button.main_menu", 
            defaultMainMenuButton.name
        ));

        this.placeBlockButton = Button.getButton(config.optString(
            "input.button.place_block", 
            defaultPlaceBlockButton.name
        ));
        this.eraseBlockButton = Button.getButton(config.optString(
            "input.button.erase_block", 
            defaultEraseBlockButton.name
        ));
        this.interactButton = Button.getButton(config.optString(
            "input.button.interact", 
            defaultInteractButon.name
        ));
        this.editButton = Button.getButton(config.optString(
            "input.button.edit", 
            defaultEditButton.name
        ));
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
