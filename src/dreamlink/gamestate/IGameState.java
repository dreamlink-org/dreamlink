package dreamlink.gamestate;

public interface IGameState {

    public abstract void onBind(); 

    public abstract void update();

    public abstract boolean showCursor();

}
