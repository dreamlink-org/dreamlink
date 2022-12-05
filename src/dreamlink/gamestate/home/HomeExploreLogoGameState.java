package dreamlink.gamestate.home;

import dreamlink.gamestate.IGameState;
import dreamlink.simulation.Simulation;
import dreamlink.window.Window;
import dreamlink.window.button.Button;

public class HomeExploreLogoGameState implements IGameState {

    private static long holdTime = 2_500;

    public static HomeExploreLogoGameState instance = new HomeExploreLogoGameState();

    private long startTime;

    @Override
    public void onBind() {
        this.startTime = System.currentTimeMillis();
    }

    public float getProgress() {
        var timeNow = System.currentTimeMillis();
        var timeElapsed = timeNow - this.startTime;
        return Math.min(1, timeElapsed / (float) HomeExploreLogoGameState.holdTime);
    }

    @Override
    public void update() {
        var timeNow = System.currentTimeMillis();
        var timeElapsed = timeNow - this.startTime;

        if(
            false
            || timeElapsed > HomeExploreLogoGameState.holdTime
            || Window.instance.isButtonPressed(Button.keyEscape)
        ) {
            Simulation.instance.setGameState(HomeExploreMenuGameState.instance);
            return;
        }
    }

    @Override
    public boolean showCursor() {
        return false;
    }
    
}
