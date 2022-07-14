package dungeonmania.Collisions;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class CollisionManager {
    private DungeonManiaController dmc;
    

    public CollisionManager(DungeonManiaController dmc) {
        this.dmc = dmc;
    }


    /**
     * is called when a moving entity attempts to move in a direction
     * checks for collisions with other objects in the direction the entity
     * wants to move.
     * 
     * If there is a collision, runs appropriate collision logic, then moves
     * the entity accordingly
     * @param entity
     * @param direction
     */
    public void requestMove(Entity entity, Direction direction) {
        // if going to collide with something
        if (dmc.getAllEntities().stream().anyMatch(x->x.getPosition() == entity.getPosition().translateBy(direction))) {
            Entity collisionEntity = dmc.getAllEntities().stream()
                .filter(x->x.getPosition() == entity.getPosition().translateBy(direction))
                .findFirst().get();
            collisionEntity.collide(entity, direction);

        } else {
            entity.getPosition().translateBy(direction);
        }
    }

    private Collision getCollision(Entity moved, Entity collided) {
        switch (moved.getType()) {
            case "Player":
                switch (collided.getType()) {
                    case "Boulder":
                        return initCollision("Push");
                    case "Door":
                        return initCollision("Unlock");


                }
        }
        return initCollision(collided.getDefaultCollision());
    }

    private Collision initCollision(String type) {
        switch (type) {
            case "Block":
                return new Block(dmc.getAllEntities());
            case "Push":
                return new Push(dmc.getAllEntities());
            case "Unlock":
                return new Unlock(dmc.getAllEntities());
                
        }
        return new Pass(dmc.getAllEntities());
    }
}