package dreamlink.player;

import org.joml.AABBf;
import org.joml.AABBi;
import org.joml.Vector3i;

import dreamlink.utility.maths.AABBiMaths;
import dreamlink.zone.block.BlockMaterial;
import dreamlink.zone.block.UserBlock;
import dreamlink.zone.terrain.TerrainBlockData;

public class PlayerSwimChecker {

    private static float buoyancyFactor = 0.5f;

    private IPlayerDirectory directory;

    public PlayerSwimChecker(IPlayerDirectory directory) {
        this.directory = directory;
    }

    public void update() {
        var state = this.directory.getPlayerState();
        var collider = state.getCollider(new AABBf());
        var height = collider.maxY - collider.minY;
        collider.maxY -= (1f - PlayerSwimChecker.buoyancyFactor) * height;

        var collisionRange = AABBiMaths.expandFrom(new AABBi(), collider);
        var blockPosition = new Vector3i();
        var blockData = new TerrainBlockData();
        var time = System.currentTimeMillis();

        for(var x = collisionRange.minX; x < collider.maxX; x += 1) {
            for(var y = collisionRange.minY; y < collider.maxY; y += 1) {
                for(var z = collisionRange.minZ; z < collider.maxZ; z += 1) {
                    state.zone.getBlockData(blockPosition.set(x, y, z), blockData);
                    var block = state.zone.getBlockByID(blockData.blockID);
                    if(block instanceof UserBlock userBlock) {
                        if(userBlock.blockMaterial == BlockMaterial.liquid) {
                            state.isSwimming = true;
                            state.lastSwimTime = time;
                            return;
                        }
                    }
                }
            }
        }

        state.isSwimming = false;
    }
    
}
