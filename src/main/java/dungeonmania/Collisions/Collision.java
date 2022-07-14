package dungeonmania.Collisions;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Direction;

public abstract class Collision {
    protected List<Entity> allEntities;
    public Collision(List<Entity> allEntities) {
        this.allEntities = allEntities;
    }

    /**
     * Processes the collision, performing logic and moving entities 
     * appropriately
     * @param moved - Entity that moved into collided
     * @param collided - Entity that was collided with
     */
    public abstract void processCollision(Entity moved, Entity collided, Direction direction);
}
