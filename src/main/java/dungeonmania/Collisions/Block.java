package dungeonmania.Collisions;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class Block extends Collision {
    public Block(List<Entity> allEntities) {
        super(allEntities);
    }

    /**
     * The entity does not move, so nothing happens
     */
    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {}
    
}
