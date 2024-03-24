package dreamlink.cli.command;

import dreamlink.gamestate.home.HomeExploreLogoGameState;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.zone.source.NexusZoneSourceStrategy;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class ExploreCommand implements ICLICommand {

    private static String commandName = "explore";

    @Override
    public String getCommandName() {
        return ExploreCommand.commandName;
    }

    @Override
    public void setupParser(Subparser subparser) {
    }

    @Override
    public void run(Namespace namespace) {
        Simulation.instance.simulationMode = SimulationMode.explore;
        Simulation.instance.zoneSourceStrategy = NexusZoneSourceStrategy.instance;
        Simulation.instance.setGameState(HomeExploreLogoGameState.instance);
        Player.instance.noClip = false;

        try {
            Simulation.instance.setup();
            Simulation.instance.run();
        } finally {
            Simulation.instance.destroy();
        }
    }
    
}
