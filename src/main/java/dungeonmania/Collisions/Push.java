package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Push extends Collision {
    
    /**
     * Precondition: moved is player, collided is boulder
     */
    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Boulder boulder = (Boulder) collided;
        Player player = (Player) moved;
        Position prevBoulderPos = boulder.getPosition();
        boulder.move(direction);
        if (boulder.getPosition() != prevBoulderPos) {
            player.setPosition(player.getPosition().translateBy(direction));
        }
    }
    
    
}
