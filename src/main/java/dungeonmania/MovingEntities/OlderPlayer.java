package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import dungeonmania.HasInventory;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.util.Position;

public class OlderPlayer extends MovingEntity implements HasInventory {
    List<InventoryObject> inventory;
    public OlderPlayer(String id, Position position, double health, double attack, JSONArray ticks, int currentTick) {
        super(id, position, false, health, attack, new OldPlayerMovement(ticks, currentTick));
        inventory = new ArrayList<>();
    }

    @Override
    public List<InventoryObject> getInventory() {
        return inventory;
    }

    
    
}
