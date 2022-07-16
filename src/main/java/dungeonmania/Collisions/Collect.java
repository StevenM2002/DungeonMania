package dungeonmania.Collisions;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.CollectibleEntity;
import dungeonmania.CollectibleEntities.InventoryObject;
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
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Player player = (Player) moved;
        CollectibleEntity collectibleEntity = (CollectibleEntity) collided;
        player.setPosition(collectibleEntity.getPosition());
        player.getInventory().add((InventoryObject) collectibleEntity.getCollectible());
        entityList.remove(collided);
    }
    
    
}