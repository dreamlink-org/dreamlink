package dreamlink.zone;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import dreamlink.Config;
import dreamlink.logger.Logger;

public class ZoneCache {

    private record ZoneCacheKey(String zoneKey, boolean isShadowCopy) { }

    public static ZoneCache instance = new ZoneCache();

    private LinkedList<ZoneCacheKey> accessList;
    private Map<ZoneCacheKey,Zone> zoneLookup;
    private Queue<Zone> retiredZones;
    private Set<Zone> allZones;

    public ZoneCache() {
        this.accessList = new LinkedList<>();
        this.zoneLookup = new HashMap<>();
        this.allZones = new HashSet<>();
        this.retiredZones = new ArrayDeque<>();
    }

    public Iterable<Zone> getZones() {
        return this.allZones;
    }

    public void clear() {
        for(var zone : this.zoneLookup.values()) {
            this.retiredZones.add(zone);
        }
        this.accessList.clear();
        this.zoneLookup.clear();
    }

    public Zone getZone(String zoneKey) {
        return this.getZone(zoneKey, false);
    }

    public Zone getZone(String zoneKey, boolean isShadowCopy) {
        var cacheKey = new ZoneCacheKey(zoneKey, isShadowCopy);
        if(!this.zoneLookup.containsKey(cacheKey)) {
            Logger.instance.info(String.format("In-memory cache miss: %s", zoneKey));
            var zone = new Zone(this, zoneKey, isShadowCopy);
            this.allZones.add(zone);
            this.zoneLookup.put(cacheKey, zone);

        // If the zone already exists, remove it from the access list (it might be
        // near the end), so that we can re-add it to the front.
        } else {
            this.accessList.remove(cacheKey);
        }

        var zone = this.zoneLookup.get(cacheKey);
        this.accessList.addFirst(cacheKey);

        // If our cache is too big, start removing zones at the end of our
        // access list (LRU behavior).
        while(this.accessList.size() > Config.instance.levelCacheSize) {
            var lastCacheKey = this.accessList.removeLast();
            var lastZone = this.zoneLookup.remove(lastCacheKey);

            // Removed zones should be placed in the retired queue, so that they
            // can be properly cleaned up.
            this.retiredZones.add(lastZone);
        }

        return zone;
    }

    public void update() {
        for(var zone : this.allZones) {
            zone.update();
        }

        while(!this.retiredZones.isEmpty()) {
            var zone = this.retiredZones.peek();
            if(zone.getZoneStatus() == ZoneStatus.ready) {
                this.retiredZones.remove().destroy();
            } else if(zone.getZoneStatus() == ZoneStatus.failed) {
                this.retiredZones.remove().destroy();
            } else {
                break;
            }
        }

    }
}