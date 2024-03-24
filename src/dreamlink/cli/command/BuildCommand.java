package dreamlink.cli.command;

import dreamlink.logger.Logger;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.FileFns;
import dreamlink.zone.ZoneCache;
import dreamlink.zone.source.LocalZoneSourceStrategy;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class BuildCommand implements ICLICommand {

    private static String commandName = "build";
    private static String directoryArg = "directory";
    private static String outputArg = "output";

    @Override
    public void setupParser(Subparser subparser) {
        subparser.addArgument(BuildCommand.directoryArg)
            .type(String.class)
            .required(true);
        
        subparser.addArgument(BuildCommand.outputArg)
            .type(String.class)
            .required(true);
    }

    @Override
    public void run(Namespace namespace) {
        var directoryPath = FileFns.sanitizeUserPath(namespace.getString(BuildCommand.directoryArg));
        var outputPath = FileFns.sanitizeUserPath(namespace.getString(BuildCommand.outputArg));

        // Load the zone data - this will run all the standard zone validation checks
        // and ensure that the zone is in a valid state before publishing...
        Logger.instance.info(String.format("Loading zone (for validation): %s", directoryPath));
        Simulation.instance.simulationMode = SimulationMode.edit;
        Simulation.instance.zoneSourceStrategy = LocalZoneSourceStrategy.instance;
        ZoneCache.instance.getZone(directoryPath.toString()).loadData();

        Logger.instance.debug(String.format("Writing zone to: %s", outputPath));
        FileFns.compressIntoZip(directoryPath.toFile(), outputPath.toFile());
    }

    @Override
    public String getCommandName() {
        return BuildCommand.commandName;
    }
    
}
