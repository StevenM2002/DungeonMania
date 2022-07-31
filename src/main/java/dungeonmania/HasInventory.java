package dungeonmania;

import java.util.List;
import dungeonmania.CollectibleEntities.InventoryObject;

public interface HasInventory {
    public List<InventoryObject> getInventory();
}
