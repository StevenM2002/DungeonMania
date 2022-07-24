package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class Block extends Collision {

    /**
     * The entity does not move, so nothing happens
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        return false;
    }
    
}
