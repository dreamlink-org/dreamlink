package dreamlink.zone.block;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.utility.maths.CubeFace;
import dreamlink.utility.maths.FloatMaths;
import dreamlink.zone.IZoneDirectory;
import dreamlink.zone.ZoneLoadException;

public class ZoneBlockSystem {
    
    private static Path blocksDirectory = Paths.get("blocks");
    private static String blockConfigSuffix = ".json";

    private static float defaultLightingAmount = 0f;
    private static boolean defaultIsAffectedByLighting = true;
    private static boolean defaultIsLightTransmitting = false;
    private static boolean defaultIsTransparent = false;
    private static boolean defaultIsHidden = false;
    private static String defaultMaterial = "solid";

    public static int maxTransparency = 15;
    public static int maxLightLevel = 15;

    private static int userBlockIDMin = 4;
    private static int userBlockIDMax = 0x3F;

    private IZoneDirectory directory;

    public AirBlock airBlock;
    public BarrierBlock barrierBlock;
    public DoorBlock doorBlock;
    public SpeakerBlock speakerBlock;

    private Map<Integer, IBlock> blockMap;
    private List<UserBlock> userBlocks;

    public ZoneBlockSystem(IZoneDirectory directory) {
        this.directory = directory;
        this.blockMap = new HashMap<>();
        this.userBlocks = new ArrayList<>();

        this.airBlock = new AirBlock();
        this.barrierBlock = new BarrierBlock(directory);
        this.doorBlock = new DoorBlock(directory);
        this.speakerBlock = new SpeakerBlock(directory);

        this.blockMap.put(airBlock.getBlockID(), airBlock);
        this.blockMap.put(barrierBlock.getBlockID(), barrierBlock);
        this.blockMap.put(doorBlock.getBlockID(), doorBlock);
        this.blockMap.put(speakerBlock.getBlockID(), speakerBlock);
    }

    public IBlock getBlockByID(int blockID) {
        return this.blockMap.get(blockID);
    }

    public int getUserBlockCount() {
        return this.userBlocks.size();
    }

    public UserBlock getUserBlockByIndex(int index) {
        return this.userBlocks.get(index);
    }

    public void loadData(Path zonePath) {
        var blocksDirectory = zonePath
            .resolve(ZoneBlockSystem.blocksDirectory)
            .toFile();

        if(!blocksDirectory.exists() || !blocksDirectory.isDirectory()) {
            return;
        }

        for(var file : blocksDirectory.listFiles(File::isFile)) {
            var fileName = file.getName();
            if(!fileName.endsWith(ZoneBlockSystem.blockConfigSuffix)) {
                continue;
            }

            var blockName = fileName.substring(
                0, 
                fileName.length() - ZoneBlockSystem.blockConfigSuffix.length()
            );

            Logger.instance.info(String.format("Loading block config: %s", fileName));
            var blockConfig = FileFns.readJSONFromFile(file);

            var lightLevel = FloatMaths.clamp(blockConfig.optFloat("lighting.amount", ZoneBlockSystem.defaultLightingAmount), 0f, 1f);
            var quantizedLightLevel = (int)(lightLevel * ZoneBlockSystem.maxLightLevel);
            var blockID = Math.max(0, blockConfig.getInt("meta.id"));

            var block = new UserBlock(
                this.directory,
                blockID,
                blockName,
                blockConfig.optBoolean("texture.is_hidden", ZoneBlockSystem.defaultIsHidden),
                blockConfig.optBoolean("texture.is_transparent", ZoneBlockSystem.defaultIsTransparent),
                quantizedLightLevel,
                blockConfig.optBoolean("lighting.is_affected", ZoneBlockSystem.defaultIsAffectedByLighting),
                blockConfig.optBoolean("lighting.is_transmitting", ZoneBlockSystem.defaultIsLightTransmitting),
                blockConfig.optString("interaction.message"),
                BlockMaterial.getBlockMaterial(blockConfig.optString("material.type", ZoneBlockSystem.defaultMaterial))
            );

            if(blockID < ZoneBlockSystem.userBlockIDMin) {
                var msg = String.format("Block ID: %d is less than minimum user block ID: %d", blockID, ZoneBlockSystem.userBlockIDMin);
                throw new ZoneLoadException(msg);
            }

            if(blockID > ZoneBlockSystem.userBlockIDMax) {
                var msg = String.format("Block ID: %d exceeds maximum user block ID: %d", blockID, ZoneBlockSystem.userBlockIDMax);
                throw new ZoneLoadException(msg);
            }

            if(this.blockMap.containsKey(block.getBlockID())) {
                var msg = String.format("Block ID: %d already exists", block.getBlockID());
                throw new ZoneLoadException(msg);
            }

            var textureSystem = this.directory.getTextureSystem();
            for(var cubeFace : CubeFace.getCubeFaces()) {
                var textureSampleKey = String.format("texture.sample.%s", cubeFace.name);
                var textureName = blockConfig.optString(textureSampleKey, null);
                var textureSample = textureSystem.getTextureSample(textureName);
                if(textureSample == null && textureName != null) {
                    var msg = String.format("Missing %s texture sample: %s", cubeFace.name, textureName);
                    Logger.instance.warn(msg);
                    continue;
                }

                block.setTextureSample(cubeFace, textureSample);
            }

            this.blockMap.put(block.getBlockID(), block);
            this.userBlocks.add(block);
        }
    }
    
}
