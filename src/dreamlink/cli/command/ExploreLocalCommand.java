package dreamlink.cli.command;

import dreamlink.gamestate.home.HomeExploreLogoGameState;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.PathFns;
import dreamlink.zone.source.LocalZoneSourceStrategy;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class ExploreLocalCommand implements ICommand {

    private static String commandName = "explore:local";
    private static String rootArg = "root";

    @Override
    public String getCommandName() {
        return ExploreLocalCommand.commandName;
    }

    @Override
    public void setupParser(Subparser subparser) {
        subparser.addArgument(ExploreLocalCommand.rootArg)
            .nargs("?")
            .setDefault("")
            .type(String.class);
    }

    @Override
    public void run(Namespace namespace) {
        var rootPathStr = namespace.getString(ExploreLocalCommand.rootArg);
        var rootPath = PathFns.sanitizeUserPath(rootPathStr);

        Simulation.instance.simulationMode = SimulationMode.explore;
        Simulation.instance.zoneSourceStrategy = new LocalZoneSourceStrategy(rootPath);
        Simulation.instance.setGameState(HomeExploreLogoGameState.instance);

        try {
            Simulation.instance.setup();
            Simulation.instance.run();
        } finally {
            Simulation.instance.destroy();
        }
    }
    
}
