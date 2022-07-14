package dungeonmania;

import dungeonmania.util.Direction;

public interface CanMove {
    /**
     * calls the request to move function from the collision manager class
     * @param direction
     */
    public void move(Direction direction);
}
