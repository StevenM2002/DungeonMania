package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.util.UtilityFunctions.getAllDirections;

public class Teleport extends Collision {

    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Player player = (Player) moved;
        Portal entryPortal = (Portal) collided;
        Position initialPlayerPos = player.getPosition();

        player.setPosition(entryPortal.getOtherPortal().getPosition());
        player.move(direction);

        // try moving in each direction if initial move didn't work
        for (int i = 0; 
            i < getAllDirections().size() 
            && player.getPosition() == entryPortal.getOtherPortal().getPosition();
            i++
        ) {
            player.move(getAllDirections().get(i));    
        }

        // if the player couldn't move, return to initial position before teleporting
        if (player.getPosition() == entryPortal.getOtherPortal().getPosition()) {
            player.setPosition(initialPlayerPos);
        }
    }
}
