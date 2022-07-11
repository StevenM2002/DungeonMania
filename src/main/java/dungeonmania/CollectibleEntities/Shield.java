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
    public double deteriorate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBattleUses() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void craft(List<InventoryObject> inventory) throws IllegalArgumentException, InvalidActionException {
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        List<InventoryObject> usedMaterial = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {    
            if (object instanceof Wood) {
                woodNo += 1;
                if (woodNo <= 2) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Treasure) {
                treasureNo += 1;
                if (treasureNo == 1) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Key) {
                keyNo += 1;
                if (keyNo == 1) {
                    usedMaterial.add(object);
                }
            }
        }
        // InvalidActionException
        if (woodNo < 2) {
            throw new InvalidActionException("Not enough wood");
        }
        if (keyNo < 1 && treasureNo < 1) {
            throw new InvalidActionException("Not enough metal");
        }
        // Crafting
        inventory.add(new Shield(super.getId(), this.defence, this.durability));
        // Removing crafting materials
        for (InventoryObject object : usedMaterial) {
            if (!(treasureNo > 0 && object instanceof Key)) { // If the player had a treasure, then don't remove the key
                inventory.remove(object);
            }
        }
    }
}