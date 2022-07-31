package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import static dungeonmania.DungeonManiaController.getDmc;
public class TimeTravel extends Collision {

    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        getDmc().queueRewind();
        return true;
    }
    
}
