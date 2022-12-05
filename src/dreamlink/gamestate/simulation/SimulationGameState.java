package dreamlink.gamestate.simulation;

import dreamlink.gamestate.IGameState;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;

public class SimulationGameState implements IGameState {

    public static SimulationGameState instance = new SimulationGameState();

    @Override
    public void onBind() {

    }

    @Override
    public void update() {
        if(Window.instance.isButtonPressed(Button.keyF4)) {
            Simulation.instance.setGameState(SimulationLogViewerMenuGameState.instance);
        } else if(Window.instance.isButtonPressed(Button.keyEscape)) {
            Simulation.instance.setGameState(SimulationMenuGameState.instance);
        }
    }
    
    @Override
    public boolean showCursor() {
        return false;
    }
}
