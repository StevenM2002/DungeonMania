package dungeonmania.CollectibleEntities;

import dungeonmania.exceptions.InvalidActionException;
import java.util.List;
import java.util.ArrayList;

public class Bow extends InventoryObject implements Buildable, Weapon, Durability {
    private int durability;

    public Bow(String id, int attackDamage, int durability) {
        super(id);
        this.durability = durability;
    }

    @Override
    public boolean deteriorate() {
        durability -= 1;
        return durability <= 0;
    }

    @Override
    public double getModifier() {
        return 2;
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        int arrowNo = 0;
        int woodNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Arrow) {
                arrowNo += 1;
            }
            if (object instanceof Wood) {
                woodNo += 1;
            }
        }
        if (arrowNo < 3 || woodNo < 1) {
            return false;
        }
        else {
            return true;
        }
    }
    
    @Override
    public void craft(List<InventoryObject> inventory) throws IllegalArgumentException, InvalidActionException {
        // InvalidActionException
        if (!canCraft(inventory)) {
            throw new InvalidActionException("Not enough materials");
        }
        // Preparing used materials for removal
        int arrowCount = 0;
        int woodCount = 0;
        List<InventoryObject> usedMaterial = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {
            if (object instanceof Arrow) {
                if (arrowCount <= 3) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Wood) {
                if (woodCount == 1) {
                    usedMaterial.add(object);
                }
            }
        }
        // Crafting
        inventory.add(this);
        // Removing crafting materials
        for (InventoryObject object : usedMaterial) {
            inventory.remove(object);
        }
    }
}