package dungeonmania.Collisions;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Direction;

public class Push extends Collision {

    public Push(List<Entity> allEntities) {
        super(allEntities);
    }
    /**
     * Precondition: moved is player, collided is boulder
     */
    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Boulder boulder = (Boulder) collided;
        Player player = (Player) moved;
        boulder.move(direction);
        if (boulder.getPosition() != player.getPosition().translateBy(direction)) {
            player.setPosition(player.getPosition().translateBy(direction));
        }
    }
    
    
}
