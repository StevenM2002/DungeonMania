package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.util.UtilityFunctions.getAllDirections;

import dungeonmania.CanMove;

public class Teleport extends Collision {

    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        CanMove moving = (CanMove) moved;
        Portal entryPortal = (Portal) collided;
        Position initialPlayerPos = moved.getPosition();

        moved.setPosition(entryPortal.getOtherPortal().getPosition());
        moving.move(direction);

        // try moving in each direction if initial move didn't work
        for (int i = 0; 
            i < getAllDirections().size() 
            && moved.getPosition() == entryPortal.getOtherPortal().getPosition();
            i++
        ) {
            moving.move(getAllDirections().get(i));    
        }

        // if the moving couldn't move, return to initial position before teleporting
        if (moved.getPosition() == entryPortal.getOtherPortal().getPosition()) {
            moved.setPosition(initialPlayerPos);
        }
        return false;
    }
}
