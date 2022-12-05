package dreamlink.graphics.mesh.strategy.terrain;

public class MeshTerrainQuadStrategy {

    private static int localLightOffset = 0;
    private static int portalLightOffset = 4;
    private static int isHiddenOffset = 8;
    private static int isAffectedByLightOffset = 9;

    private IMeshTerrainMetaDataStrategyProvider provider;

    public MeshTerrainQuadStrategy(IMeshTerrainMetaDataStrategyProvider provider) {
        this.provider = provider;
    }

    public void add(
        int localLight, 
        int portalLight, 
        boolean isHidden,
        boolean isAffectedByLight
    ) {
        var buffer = this.provider.getTerrainMetaDataBuffer();
        var packedValue = 0;
        packedValue |= localLight << MeshTerrainQuadStrategy.localLightOffset;
        packedValue |= portalLight << MeshTerrainQuadStrategy.portalLightOffset;
        packedValue |= (isHidden ? 1 : 0) << MeshTerrainQuadStrategy.isHiddenOffset;
        packedValue |= (isAffectedByLight ? 1 : 0) << MeshTerrainQuadStrategy.isAffectedByLightOffset;
        for(var ix = 0; ix < 4; ix += 1) {
            buffer.add(packedValue);
        }
    }
    
}
