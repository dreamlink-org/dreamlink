package dreamlink.gamestate.simulation;

import dreamlink.gamestate.IGameState;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;

public class SimulationMenuGameState implements IGameState {

    public static SimulationMenuGameState instance = new SimulationMenuGameState();

    @Override
    public void onBind() {
        
    }

    @Override
    public void update() {
        if(Window.instance.isButtonPressed(Button.keyEscape)) {
            Simulation.instance.setGameState(SimulationGameState.instance);
        }
        if(Window.instance.isButtonPressed(Button.keyF4)) {
            Simulation.instance.setGameState(SimulationLogViewerMenuGameState.instance);
        }
    }

    @Override
    public boolean showCursor() {
        return true;
    }
    
}
