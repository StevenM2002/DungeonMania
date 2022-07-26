package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Push extends Collision {
    
    /**
     * Precondition: moved is player, collided is boulder
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        Boulder boulder = (Boulder) collided;
        Position prevBoulderPos = boulder.getPosition();
        boulder.move(direction);
        if (boulder.getPosition() != prevBoulderPos) {
            return true;
        }
        return false;
    }
    
    
}
