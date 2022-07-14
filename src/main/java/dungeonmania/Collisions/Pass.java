package dungeonmania.Collisions;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class Pass extends Collision {

    public Pass(List<Entity> allEntities) {
        super(allEntities);
    }

    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        moved.setPosition(moved.getPosition().translateBy(direction));
    }
    
    
}
