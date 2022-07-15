package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.util.Position;

import java.util.List;

public class FriendlyMovement implements Movement, PlayerListener {
    private Position prevPlayerPos;
    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        //TODO Move player before mercenary
        return prevPlayerPos;
    }

    @Override
    public void update(PlayerDataArgs data) {
        prevPlayerPos = data.getPreviousPlayerPosition();
    }
}
