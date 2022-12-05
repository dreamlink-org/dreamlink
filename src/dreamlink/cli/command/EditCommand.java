package dreamlink.cli.command;

import dreamlink.gamestate.home.HomeLoadZoneGameState;
import dreamlink.logger.Logger;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.PathFns;
import dreamlink.zone.ZoneCache;
import dreamlink.zone.source.LocalZoneSourceStrategy;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class EditCommand implements ICommand {

    private static String commandName = "edit";
    private static String directoryArg = "directory";

    @Override
    public void setupParser(Subparser subparser) {
        subparser.addArgument(EditCommand.directoryArg)
            .type(String.class)
            .required(true);
    }

    @Override
    public void run(Namespace namespace) {
        var zonePathStr = namespace.getString(EditCommand.directoryArg);
        var zonePath = PathFns.sanitizeUserPath(zonePathStr);

        var zoneName = zonePath.toString();
        Logger.instance.info(String.format("Editing zone: %s", zoneName));

        Simulation.instance.zoneSourceStrategy = LocalZoneSourceStrategy.instance;
        Simulation.instance.simulationMode = SimulationMode.edit;
        Simulation.instance.setGameState(HomeLoadZoneGameState.instance);
        Player.instance.setZone(ZoneCache.instance.getZone(zoneName));

        try {
            Simulation.instance.setup();
            Simulation.instance.run();
        } finally {
            Simulation.instance.destroy();
        }
    }

    @Override
    public String getCommandName() {
        return EditCommand.commandName;
    }
    
}
