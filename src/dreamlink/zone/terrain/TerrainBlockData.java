package dreamlink.zone.terrain;

import dreamlink.zone.terrain.light.LightType;

public class TerrainBlockData {

    private static int blockIDOffset = 0;
    private static int blockIDMask = 0x3F;
    private static int orientationIDOffset = 6;
    private static int orientationIDMask = 0x3;
    private static int localLightOffset = 8;
    private static int portalLightOffset = 12;
    private static int lightMask = 0xF;

    public int blockID;
    public int orientationID;
    public int localLight;
    public int portalLight;

    public TerrainBlockData(
        int blockID, 
        int orientationID, 
        int localLight, 
        int portalLight
    ) {
        this.set(blockID, orientationID, localLight, portalLight);
    }

    public TerrainBlockData() {
        this.clear();
    }

    public TerrainBlockData set(int blockID, int orientationID, int localLight, int portalLight) {
        this.blockID = blockID;
        this.orientationID = orientationID;
        this.localLight = localLight;
        this.portalLight = portalLight;
        return this;
    }

    public TerrainBlockData unpack(short value) {
        return this.set(
            (value >> blockIDOffset) & blockIDMask,
            (value >> orientationIDOffset) & orientationIDMask,
            (value >> localLightOffset) & lightMask,
            (value >> portalLightOffset) & lightMask
        );
    }

    public short pack() {
        short packedValue = 0;
        packedValue += this.blockID << blockIDOffset;
        packedValue += this.orientationID << orientationIDOffset;
        packedValue += this.localLight << localLightOffset;
        packedValue += this.portalLight << portalLightOffset;
        return packedValue;
    }

    public TerrainBlockData clear() {
        return this.set(0, 0, 0, 0);
    }

    public int getLight(LightType lightType) {
        if(lightType == LightType.local) {
            return this.localLight;
        } else if(lightType == LightType.portal) {
            return this.portalLight;
        } else {
            return 0;
        }
    }

    public void setLight(LightType lightType, int value) {
        if(lightType == LightType.local) {
            this.localLight = value;
        } else if(lightType == LightType.portal) {
            this.portalLight = value;
        }
    }

}
