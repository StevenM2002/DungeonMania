package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.util.Position;

import java.util.List;

public class FriendlyMovement implements Movement {
    private Position prevPlayerPos;
    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        //TODO Move player before mercenary
        return player.getPreviousPosition();
    }
}
