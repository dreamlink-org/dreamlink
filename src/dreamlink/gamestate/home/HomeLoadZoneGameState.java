package dreamlink.gamestate.home;

import dreamlink.gamestate.IGameState;
import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.ZoneStatus;

public class HomeLoadZoneGameState implements IGameState {

    public static HomeLoadZoneGameState instance = new HomeLoadZoneGameState();

    @Override
    public void onBind() {
    }

    @Override
    public void update() {
        var zoneStatus = Player.instance.getZone().getZoneStatus();
        if(zoneStatus == ZoneStatus.ready) {
            Simulation.instance.setGameState(SimulationGameState.instance);
            Player.instance.spawn();
            return;
        }

        if(
            true
            && Window.instance.isButtonDown(Button.keyLeftAlt)
            && Window.instance.isButtonPressed(Button.keyQ)
        ) {
            Window.instance.setShouldClose();
        }
    }

    @Override
    public boolean showCursor() {
        return true;
    }

}
