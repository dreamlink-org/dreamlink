package dreamlink.zone;

import dreamlink.zone.block.ZoneBlockSystem;
import dreamlink.zone.door.ZoneDoorSystem;
import dreamlink.zone.global.ZoneGlobalConfigSystem;
import dreamlink.zone.speaker.ZoneSpeakerSystem;
import dreamlink.zone.terrain.ZoneTerrainSystem;

public interface IZoneDirectory {

    public ZoneCache getZoneCache();

    public ZoneTextureSystem getTextureSystem();

    public ZoneTerrainSystem getTerrainSystem();

    public ZoneDoorSystem getDoorSystem();

    public ZoneSoundSystem getSoundSystem();

    public ZoneSpeakerSystem getSpeakerSystem();

    public ZoneGlobalConfigSystem getGlobalConfigSystem();

    public ZoneBlockSystem getBlockSystem();

    public boolean isShadowCopy();

    public String getName();

}
