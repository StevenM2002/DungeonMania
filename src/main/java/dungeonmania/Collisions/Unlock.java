package dungeonmania.Collisions;


import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Key;
import dungeonmania.CollectibleEntities.SunStone;
import dungeonmania.StaticEntities.Door;
import dungeonmania.util.Direction;

public class Unlock extends Collision {

    /**
     * preconditions: moved is Player, collided is Door
     * checks if the player has the key in their inventory, if so unlocks the
     * door and moves, otherwise is blocked
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        Player player = (Player) moved;
        Door door = (Door) collided;
        Key playerKey = player.getInventory().stream()
            .filter(x->(x instanceof Key))
            .map(x->(Key) x)
            .filter(x->door.keyMatchesDoor(x))
            .findFirst()
            .orElseGet(()->{return null;});
        SunStone playerSunstone = player.getInventory().stream()
            .filter(x->(x instanceof SunStone))
            .map(x->(SunStone) x)
            .findFirst()
            .orElseGet(()->{return null;});
        if (door.isLocked() && (playerKey != null || playerSunstone != null)) {
            System.out.println("unlocking door");
            door.unlock();
            if (playerSunstone == null) {
                player.getInventory().remove(playerKey);
            }
        }
        
        if (!door.isLocked()) {
            return true;
        }
        return false;
    }
    
}
