package dreamlink.zone.source;

import java.nio.file.Path;
import java.nio.file.Paths;

import dreamlink.zone.ZoneLoadException;

public class LocalZoneSourceStrategy implements IZoneSourceStrategy {

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

    public static LocalZoneSourceStrategy instance = new LocalZoneSourceStrategy(
        Paths.get("/")
    );

    public Path root;

    public LocalZoneSourceStrategy(Path root) {
        this.root = root;
    }

    @Override
    public IZoneData fetchZoneData(String path) {
        var fullPath = this.root.resolve(path);
        if(!fullPath.toFile().exists()) {
            var msg = String.format("Zone not found: %s", fullPath);
            throw new ZoneLoadException(msg);
        }
        return new InternalZoneData(fullPath);
    }
    
}
