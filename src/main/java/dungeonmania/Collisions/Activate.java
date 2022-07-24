package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Switch;
import dungeonmania.util.Direction;

public class Activate extends Collision {

    /**
     * Precondition: collided implements Switch
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        Switch switchEntity = (Switch) collided;
        switchEntity.setActivated(true);
        return true;
    }    
}
