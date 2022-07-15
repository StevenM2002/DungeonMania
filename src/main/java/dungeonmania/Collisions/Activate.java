package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Switch;
import dungeonmania.util.Direction;

public class Activate extends Collision {

    /**
     * Precondition: collided implements Switch
     */
    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Switch switchEntity = (Switch) collided;
        moved.setPosition(collided.getPosition());
        switchEntity.setActivated(true);
    }    
}
