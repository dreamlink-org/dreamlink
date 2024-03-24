package dreamlink.cli.command;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Paths;

import dreamlink.Config;
import dreamlink.logger.Logger;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.FileFns;
import dreamlink.zone.ZoneCache;
import dreamlink.zone.source.LocalZoneSourceStrategy;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class PublishCommand implements ICLICommand {

    private static String commandName = "publish";
    private static String zoneNameKey = "meta.nexusid";
    private static String directoryArg = "directory";
    private static String dreamCodeHeader = "X-Nexus-Auth";

    @Override
    public void setupParser(Subparser subparser) {
        subparser.addArgument(PublishCommand.directoryArg)
            .type(String.class)
            .required(true);
    }

    @Override
    public void run(Namespace namespace) {
        var directoryPath = FileFns.sanitizeUserPath(namespace.getString(PublishCommand.directoryArg));
        try {
            var tempFile = File.createTempFile("zone", ".zip");
            try {
                var zoneConfigPath = directoryPath.resolve("zone.json");
                var zoneConfig = FileFns.readJSONFromFile(zoneConfigPath.toFile());
                var uploadPath = Paths.get(Config.instance.nexusRoot.getPath(), zoneConfig.getString(PublishCommand.zoneNameKey)).toString();
                var uploadURI = Config.instance.nexusRoot.resolve(uploadPath);
                var uploadURL = uploadURI.toURL();

                // Load the zone data - this will run all the standard zone validation checks
                // and ensure that the zone is in a valid state before publishing...
                Logger.instance.info(String.format("Loading zone (for validation): %s", directoryPath));
                Simulation.instance.simulationMode = SimulationMode.edit;
                Simulation.instance.zoneSourceStrategy = LocalZoneSourceStrategy.instance;
                ZoneCache.instance.getZone(directoryPath.toString()).loadData();

                Logger.instance.info("Building zone");
                FileFns.compressIntoZip(directoryPath.toFile(), tempFile);
                var bytes = FileFns.readBytesFromFile(tempFile);


                Logger.instance.info("Uploading zone");
                var connection = (HttpURLConnection)uploadURL.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/zip");
                connection.setRequestProperty(PublishCommand.dreamCodeHeader, Config.instance.nexusDreamCode);

                try(var outputStream = connection.getOutputStream()) {
                    outputStream.write(bytes);
                }

                var responseCode = connection.getResponseCode();
                if(responseCode != 200) {
                    throw new RuntimeException("Failed to publish zone");
                }
            } finally {
                tempFile.delete();
            }
        } catch (IOException e) {
            Logger.instance.error(e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return PublishCommand.commandName;
    }
    
}
