package dreamlink.cli.command;

import dreamlink.gamestate.home.HomeExploreLogoGameState;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.PathFns;
import dreamlink.zone.source.LocalMockNexusZoneSourceStrategy;
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
            .nargs("+")
            .setDefault("")
            .type(String.class);
    }

    @Override
    public void run(Namespace namespace) {
        var strategy = new LocalMockNexusZoneSourceStrategy();
        var rootStrings = namespace.getList(ExploreLocalCommand.rootArg);
        for(var rootString : rootStrings) {
            var rootPath = PathFns.sanitizeUserPath((String)rootString);
            strategy.scanDirectory(rootPath.toFile());
        }

        Simulation.instance.simulationMode = SimulationMode.explore;
        Simulation.instance.zoneSourceStrategy = strategy;
        Simulation.instance.setGameState(HomeExploreLogoGameState.instance);

        try {
            Simulation.instance.setup();
            Simulation.instance.run();
        } finally {
            Simulation.instance.destroy();
        }
    }
    
}
