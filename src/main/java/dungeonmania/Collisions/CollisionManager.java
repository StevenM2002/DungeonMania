package dungeonmania.Collisions;

import java.util.stream.Stream;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.StaticEntities.Switch;
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
    public void requestMove(Entity moved, Direction direction) {
        Stream<Entity> collidingEntities = dmc.getAllEntities().stream()
            .filter(x->x.getPosition() == moved.getPosition().translateBy(direction));
        
        // check if the entity is blocked, otherwise process all collisions
        // does this to prevent battles with spiders in walls, etc
        if (!collidingEntities.anyMatch(x->(getCollision(moved, x) instanceof Block))) {
            collidingEntities.forEach(x->getCollision(moved, x).processCollision(moved, x, direction));
        }
    }

    /**
     * Matches the two entities with the correct collision type
     * @param moved
     * @param collided
     * @return
     */
    private Collision getCollision(Entity moved, Entity collided) {
        switch (moved.getType()) {
            case "Player":
                switch (collided.getType()) {
                    case "Boulder":
                        return initCollision("Push");
                    case "Door":
                        return initCollision("Unlock");
                    case "Portal":
                        return initCollision("Teleport");
                    case "Spider":
                        return initCollision("Battle");
                    case "Mercenary":
                        Mercenary merc = (Mercenary) collided;
                        if (merc.isFriendly()) {
                            return initCollision("Pass");
                        }
                        return initCollision("Battle");
                    case "ZombieToast":
                        return initCollision("Battle");
                    case "Exit":
                        return initCollision("Activate");
                    case "Arrow":
                        return initCollision("Collect");
                    case "Bomb":
                        return initCollision("Collect");
                    case "InvincibilityPotion":
                        return initCollision("Collect");
                    case "InvisibilityPotion":
                        return initCollision("Collect");
                    case "Key":
                        return initCollision("Collect");
                    case "Sword":
                        return initCollision("Collect");
                    case "Treasure":
                        return initCollision("Collect");
                    case "Wood":
                        return initCollision("Collect");
                }
            case "Mercenary":
                switch (collided.getType()) {
                    case "Portal":
                        return initCollision("Teleport");
                    case "Player":
                        Mercenary merc = (Mercenary) moved;
                        if (merc.isFriendly()) return initCollision("Block");
                        
                }
            case "Spider":
                switch (collided.getType()) {
                    case "Wall":
                        return initCollision("Pass");
                    case "Portal":
                        return initCollision("Pass");
                    case "Door":
                        return initCollision("Pass");
                }
            case "Boulder":
                switch (collided.getType()) {
                    case "FloorSwitch":
                        return initCollision("Activate");
                }
        }
        return initCollision(collided.getDefaultCollision());
    }

    /**
     * initialises a new collision of the given type.
     * useful so that each entity doesn't have to know the collision type
     * for their default collisions.
     * @param type
     * @return
     */
    private Collision initCollision(String type) {
        switch (type) {
            case "Block":
                return new Block();
            case "Push":
                return new Push();
            case "Unlock":
                return new Unlock();
            case "Teleport":
                return new Teleport();
            case "Battle":
                return new Battle(dmc.getBattleManager());
            case "Collect":
                return new Collect(dmc.getAllEntities());
            case "Activate":
                return new Activate();
        }
        return new Pass();
    }

    /**
     * Deactivates all switches that do not have their activation type on 
     * top of them
     */
    public void deactivateSwitches() {
        Stream<Switch> switchStream = dmc.getAllEntities().stream()
            .filter(x->(x instanceof Switch))
            .map(x->(Switch) x);
        switchStream.forEach(x->{
            Entity e = (Entity) x;
            if (!dmc.getAllEntities()
                .stream()
                .anyMatch(y->
                    (y.getPosition() == e.getPosition() 
                    && y.getType() == x.getActivationType()
                ))
                && x.getActivated()
            ) {
                x.setActivated(false);
            }
        });   
    }
}