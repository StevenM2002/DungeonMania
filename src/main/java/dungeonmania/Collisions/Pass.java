package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class Pass extends Collision {


    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        moved.setPosition(collided.getPosition());
    }
    
    
}
