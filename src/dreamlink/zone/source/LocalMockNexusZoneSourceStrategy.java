package dreamlink.zone.source;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.zone.ZoneLoadException;

public class LocalMockNexusZoneSourceStrategy implements IZoneSourceStrategy {

    private class InternalZoneData implements IZoneData {

        private Path path;

        public InternalZoneData(Path path) {
            this.path = path;
        }

        @Override
        public Path getPath() {
            return this.path;
        }

        @Override
        public void close() {
        }

    }

    private Map<String, Path> zonePaths;

    public LocalMockNexusZoneSourceStrategy() {
        this.zonePaths = new HashMap<>();
    }

    public void scanDirectory(File rootDirectory) {
        for(var childDirectory : rootDirectory.listFiles(File::isDirectory)) {
            var zoneFile = new File(childDirectory, "zone.json");
            if(!zoneFile.exists()) {
                continue;
            }
            try {
                var zoneConfig = FileFns.readJSONFromFile(zoneFile);
                var zoneName = zoneConfig.getString("meta.nexusid");
                var msg = String.format("Discovered zone: %s", zoneName);
                Logger.instance.debug(msg);
                this.zonePaths.put(zoneName, childDirectory.toPath());
            } catch(JSONException e) {
                var msg = String.format("Error reading zone config: %s", zoneFile);
                Logger.instance.warn(msg);
            }
        }
    }

    @Override
    public IZoneData fetchZoneData(String name) {
        if(!this.zonePaths.containsKey(name)) {
            var msg = String.format("Zone not found: %s", name);
            throw new ZoneLoadException(msg);
        }
        var zonePath = this.zonePaths.get(name);
        return new InternalZoneData(zonePath);
    }
    
}
