package dreamlink.zone.terrain.light;

import org.joml.Vector3i;

public class LightVoxel {

    public Vector3i position;
    public int localLight;
    public int portalLight;

    public LightVoxel() {
        this.position = new Vector3i();
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
