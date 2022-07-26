package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class Pass extends Collision {


    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        return true;
    }
    
    
}
