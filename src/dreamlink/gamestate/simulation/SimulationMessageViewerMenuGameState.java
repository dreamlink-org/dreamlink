package dreamlink.gamestate.simulation;

import dreamlink.gamestate.IGameState;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;

public class SimulationMessageViewerMenuGameState implements IGameState {

    public static SimulationMessageViewerMenuGameState instance = new SimulationMessageViewerMenuGameState();

    @Override
    public void onBind() {

    }

    @Override
    public void update() {
        if(Window.instance.isButtonPressed(Button.keyEscape)) {
            Simulation.instance.setGameState(SimulationGameState.instance);
        }
    }

    @Override
    public boolean showCursor() {
        return true;
    }
    
}
