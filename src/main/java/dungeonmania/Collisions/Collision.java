package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.util.Direction;

public abstract class Collision {
    /**
     * Processes the collision, performing logic and moving entities 
     * appropriately
     * @param moved - Entity that moved into collided
     * @param collided - Entity that was collided with
     * @return returns true if the entity should pass, false if blocked
     */
    public abstract boolean processCollision(Entity moved, Entity collided, Direction direction);
}
