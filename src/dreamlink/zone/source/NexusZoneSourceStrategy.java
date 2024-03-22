package dreamlink.zone.source;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import dreamlink.Config;
import dreamlink.logger.Logger;
import dreamlink.utility.FileFns;
import dreamlink.zone.ZoneLoadException;

public class NexusZoneSourceStrategy implements IZoneSourceStrategy {

    private class InternalZoneData implements IZoneData {

        private Path temporaryPath;

        public InternalZoneData(Path temporaryPath) {
            this.temporaryPath = temporaryPath;
        }

        @Override
        public Path getPath() {
            return this.temporaryPath;
        }

        @Override
        public void close() {
            FileFns.deleteDirectory(this.temporaryPath.toFile());
        }

    }

    public static NexusZoneSourceStrategy instance = new NexusZoneSourceStrategy();

    @Override
    public IZoneData fetchZoneData(String name) {
        var nexusRoot = Config.instance.nexusRoot;
        var nexusPath = Paths.get(nexusRoot.getPath(), name).toString();
        var zoneURI = nexusRoot.resolve(nexusPath.toString());

        try {
            var tempDirPath = Files.createTempDirectory("zone");
            var tempFile = File.createTempFile("zone.zip", null);
            try(
                var inputStream = zoneURI.toURL().openStream();
                var outputStream = new FileOutputStream(tempFile);
            ) {
                Logger.instance.debug(String.format("Downloading zone: %s", zoneURI));
                inputStream.transferTo(outputStream);
                FileFns.extractZip(tempFile, tempDirPath.toFile());
                return new InternalZoneData(tempDirPath);
            } finally {
                tempFile.delete();
            }
        } catch (IOException e) {
            var msg = String.format("Failed to download zone: %s", zoneURI);
            throw new ZoneLoadException(msg);
        }
    }
    
}
