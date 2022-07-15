package dungeonmania;

import java.util.List;

import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.response.models.DungeonResponse;

public interface Interactable {
    public DungeonResponse interact(List<InventoryObject> inventory, List<Entity> allEntities);
}
