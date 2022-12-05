package dreamlink.gamestate.home;

import dreamlink.gamestate.IGameState;
import dreamlink.menu.component.home.HomeExploreComponent;
import dreamlink.player.Player;
import dreamlink.zone.ZoneCache;

public class HomeExploreMenuGameState implements IGameState {

    public static HomeExploreMenuGameState instance = new HomeExploreMenuGameState();

    @Override
    public void onBind() {
        Player.instance.setZone(null);
        ZoneCache.instance.clear();
        HomeExploreComponent.instance.clear();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean showCursor() {
        return true;
    }
    
}
