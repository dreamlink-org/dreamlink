package dreamlink.gamestate.simulation;

import dreamlink.gamestate.IGameState;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.utility.worker.WorkerPool;
import dreamlink.utility.worker.WorkerTask;

public class SimulationZoneSaveBusyGameState implements IGameState {

    public static SimulationZoneSaveBusyGameState instance = new SimulationZoneSaveBusyGameState();

    private WorkerTask saveTask;

    @Override
    public void onBind() {
        this.saveTask = WorkerPool.instance.submit(Player.instance.getZone()::saveZone);
    }

    @Override
    public void update() {
        if(this.saveTask.isDone()) {
            this.saveTask.join();
            Simulation.instance.setGameState(SimulationMenuGameState.instance);
        }
    }

    @Override
    public boolean showCursor() {
        return true;
    }
    
}
