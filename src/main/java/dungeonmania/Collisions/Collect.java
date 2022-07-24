package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.CollectibleEntity;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.Key;
import dungeonmania.util.Direction;
import java.util.List;

public class Collect extends Collision {
    List<Entity> entityList;
    
    public Collect(List<Entity> entityList) {
        this.entityList = entityList;
    }


    /**
     * Precondition: moved is Player, collided is CollectibleEntity
     */
    @Override
    public boolean processCollision(Entity moved, Entity collided, Direction direction) {
        Player player = (Player) moved;
        CollectibleEntity collectibleEntity = (CollectibleEntity) collided;
        if (!(collectibleEntity.getCollectible() instanceof Key && player.getInventory().stream().anyMatch(x->x instanceof Key))) {
            player.getInventory().add((InventoryObject) collectibleEntity.getCollectible());
            entityList.remove(collided);
        }
        return true;
    }
    
    
}
