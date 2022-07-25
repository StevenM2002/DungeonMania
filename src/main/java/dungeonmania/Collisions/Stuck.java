package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;

public class Stuck extends Collision {
    /**
     * @precondition moved instanceOf MovingEntity
     * @precondition collided instanceOf SwampTile
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        MovingEntity moving = (MovingEntity) moved;
        SwampTile swamp = (SwampTile) collided;
        moving.setStuckAmount(swamp.getMovementFactor());
        return true;
    }
    
}
