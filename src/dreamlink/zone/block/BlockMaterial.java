package dreamlink.zone.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockMaterial {

    private static Map<String, BlockMaterial> blockMaterialLookup = new HashMap<>();
    private static List<BlockMaterial> blockMaterials = new ArrayList<>();

    public static BlockMaterial getBlockMaterial(String name) {
        return BlockMaterial.blockMaterialLookup.get(name);
    }

    public static BlockMaterial getBlockMaterialByIndex(int index) {
        return BlockMaterial.blockMaterials.get(index);
    }

    public static int getBlockMaterialCount() {
        return BlockMaterial.blockMaterials.size();
    }

    public static BlockMaterial empty = new BlockMaterial("empty");
    public static BlockMaterial liquid = new BlockMaterial("liquid");
    public static BlockMaterial solid = new BlockMaterial("solid");

    public String name;

    public BlockMaterial(String name) {
        this.name = name;
        BlockMaterial.blockMaterialLookup.put(name, this);
        BlockMaterial.blockMaterials.add(this);
    }
}
