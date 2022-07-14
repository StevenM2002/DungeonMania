package dungeonmania.Collisions;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Door;
import dungeonmania.util.Direction;

public class Unlock extends Collision {

    public Unlock(List<Entity> allEntities) {
        super(allEntities);
    }

    /**
     * preconditions: moved is Player, collided is Door
     * checks if the player has the key in their inventory, if so unlocks the
     * door and moves, otherwise is blocked
     */
    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Player player = (Player) moved;
        Door door = (Door) collided;
        if (door.isLocked() && )
    }
    
}
