package dungeonmania.CollectibleEntities;

import dungeonmania.exceptions.InvalidActionException;
import java.util.List;
import java.util.ArrayList;

public class Shield extends InventoryObject implements Buildable, Durability {
    private int defence;
    private int durability;

    public Shield(String id, int defence, int durability) {
        super(id);
        this.defence = defence;
        this.durability = durability;
    }

    @Override
    public boolean deteriorate() {
        durability -= 1;
        return durability <= 0;
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        int treasureNo = 0;
        int woodNo = 0;
        int keyNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Wood) {
                woodNo += 1;
            }
            if (object instanceof Treasure) {
                treasureNo += 1;
            }
            if (object instanceof Key) {
                keyNo += 1;
            }
        }
        if ((keyNo < 1 && treasureNo < 1) || woodNo < 2) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getDefence() {
        return defence;
    }

    @Override
    public void craft(List<InventoryObject> inventory) throws IllegalArgumentException, InvalidActionException {
        // InvalidActionException
        if (!canCraft(inventory)) {
            throw new InvalidActionException("Not enough materials");
        }
        // Preparing materials for removal
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        List<InventoryObject> usedMaterial = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {    
            if (object instanceof Wood) {
                if (woodNo <= 2) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Treasure) {
                if (treasureNo == 1) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Key) {
                if (keyNo == 1) {
                    usedMaterial.add(object);
                }
            }
        }
        // Crafting
        inventory.add(this);
        // Removing crafting materials
        for (InventoryObject object : usedMaterial) {
            if (!(treasureNo > 0 && object instanceof Key)) { // If the player had a treasure, then don't remove the key
                inventory.remove(object);
            }
        }
    }
}