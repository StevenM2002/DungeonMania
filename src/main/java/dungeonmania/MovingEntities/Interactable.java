package dungeonmania.MovingEntities;

import dungeonmania.Player;
import dungeonmania.exceptions.InvalidActionException;

public interface Interactable {
    public void interact(Player player) throws InvalidActionException;
}
