package dungeonmania.Collisions;

import java.util.ArrayList;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.PotionManager;
import dungeonmania.MovingEntities.Assassin;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.OlderPlayer;
import dungeonmania.StaticEntities.Switch;
import dungeonmania.StaticEntities.LogicalEntity;
import dungeonmania.StaticEntities.LogicalSwitch;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.DungeonManiaController.getDmc;

public class CollisionManager {
    /**
     * is called when a moving entity attempts to move in a direction
     * checks for collisions with other objects in the direction the entity
     * wants to move.
     * 
     * If there is a collision, runs appropriate collision logic, then moves
     * the entity accordingly
     * @param moved
     * @param direction
     */
    public static void requestMove(Entity moved, Direction direction) {
        Position toMove = moved.getPosition().translateBy(direction);
        ArrayList<Entity> collisionQueue = new ArrayList<>();
        getDmc().getAllEntities().stream()
            .filter(x->x.getPosition().equals(toMove) && x.getId() != moved.getId())
            .forEach(x->collisionQueue.add(x));
        boolean doMove = true;
        for (Entity collided : collisionQueue) {
            if (!getCollision(moved, collided).processCollision(moved, collided, direction)) {
                doMove = false;
            }
        }
        if (doMove) {
            moved.setPosition(toMove);
        }
    }

    /**
     * Matches the two entities with the correct collision type
     * @param moved
     * @param collided
     * @return type Block if it is blocking else random shit
     */
    public static Collision getCollision(Entity moved, Entity collided) {
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
                    case "Assassin":
                        Assassin ass = (Assassin) collided;
                        if (ass.isFriendly()) {
                            return initCollision("Pass");
                        }
                        return initCollision("Battle");
                    case "ZombieToast":
                        return initCollision("Battle");
                    case "Hydra":
                        return initCollision("Battle");
                    case "Exit":
                        return initCollision("Activate");

                    // collectible entities
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
                    case "SunStone":
                        return initCollision("Collect");
                    case "SwampTile":
                        return initCollision("Pass");
                    case "OlderPlayer":
                        Player p = (Player) moved;
                        OlderPlayer op = (OlderPlayer) collided;
                        if (p.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("sun_stone"))
                        || p.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("midnight_armour"))
                        || op.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("sun_stone"))
                        || op.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("midnight_armour"))
                        || (PotionManager.getCurrPotion() != null 
                        && PotionManager.getCurrPotion().getName().equals("invisibility_potion"))
                        ) {
                            return initCollision("Pass");
                        }
                        return initCollision("Battle");
                    case "TimeTurner":
                        return initCollision("Collect");
                    case "TimeTravellingPortal":
                        return initCollision("TimeTravel");

                }
                break;
            case "Mercenary":
                switch (collided.getType()) {
                    case "Portal":
                        return initCollision("Teleport");
                    case "Player":
                        Mercenary merc = (Mercenary) moved;
                        if (merc.isFriendly()) return initCollision("Block");
                        break;
                }
                break;
            case "Assassin":
                switch (collided.getType()) {
                    case "Portal":
                        return initCollision("Teleport");
                    case "Player":
                        Assassin ass = (Assassin) moved;
                        if (ass.isFriendly()) return initCollision("Block");
                        break;
                }
                break;
            case "Spider":
                switch (collided.getType()) {
                    case "Wall":
                        return initCollision("Pass");
                    case "Portal":
                        return initCollision("Pass");
                    case "Door":
                        return initCollision("Pass");
                    case "ActiveBomb":
                        return initCollision("Pass");
                }
                break;
            case "Boulder":
                switch (collided.getType()) {
                    case "switch":
                        return initCollision("Activate");
                    case "SwampTile":
                        return initCollision("Pass");
                }
                break;
            case "OlderPlayer":
                System.out.println("older player colliding with "+collided.getType());
                switch (collided.getType()) {
                    case "Boulder":
                        return initCollision("Push");
                    case "Door":
                        return initCollision("Unlock");
                    case "Portal":
                        return initCollision("Teleport");
                    // collectible entities
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
                    case "Sunstone":
                        return initCollision("Collect");
                    case "SwampTile":
                        return initCollision("Pass");
                    case "OlderPlayer":
                        Player p = (Player) collided;
                        OlderPlayer op = (OlderPlayer) moved;
                        if (p.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("sun_stone"))
                        || p.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("midnight_armour"))
                        || op.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("sun_stone"))
                        || op.getInventory().stream().anyMatch(x->x.getItemResponse().getType().equals("midnight_armour"))
                        || (PotionManager.getCurrPotion() != null 
                        && PotionManager.getCurrPotion().getName().equals("invisibility_potion"))
                        ) {
                            return initCollision("Pass");
                        }
                        return initCollision("Battle");
                    case "TimeTurner":
                        return initCollision("Collect");

                }
            break;
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
    private static Collision initCollision(String type) {
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
                return new Battle(getDmc().getBattleManager());
            case "Collect":
                return new Collect(getDmc().getAllEntities());
            case "Activate":
                return new Activate();
            case "Stuck":
                return new Stuck();
            case "TimeTravel":
                return new TimeTravel();
        }
        return new Pass();
    }

    /**
     * Deactivates all switches that do not have their activation type on 
     * top of them
     */
    public static void deactivateSwitches() {
        getDmc().getAllEntities().stream()
            .filter(x->((x instanceof Switch && !(x instanceof LogicalEntity))) || x instanceof LogicalSwitch)
            .map(x->(Switch) x)
            .forEach(x->{
                Entity e = (Entity) x;
                if (!getDmc().getAllEntities()
                    .stream()
                    .anyMatch(y->
                        (y.getPosition().equals(e.getPosition())
                        && y.getType().equals(x.getActivationType())
                    ))
                    && x.getActivated()
                ) {
                    x.setActivated(false);
                }
            }
        );   
    }
}