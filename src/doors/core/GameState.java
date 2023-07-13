package doors.core;

public abstract class GameState {

    public static GameState USED_GAME_STATE = null;

    public void use() {
        USED_GAME_STATE = this;
    }

    public abstract void update();
}