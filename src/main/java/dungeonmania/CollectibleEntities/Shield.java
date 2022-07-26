package dungeonmania.CollectibleEntities;

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
    public boolean canCraft(List<InventoryObject> inventory, boolean hasZombies) {
        int treasureNo = 0;
        int woodNo = 0;
        int keyNo = 0;
        int sunstoneNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Wood) {
                woodNo += 1;
            }
            if (object instanceof Treasure) {
                treasureNo += 1;
            }
            if (object instanceof Sunstone) {
                sunstoneNo += 1;
            }
            if (object instanceof Key) {
                keyNo += 1;
            }
        }
        if ((keyNo < 1 && treasureNo < 1 && sunstoneNo < 1) || woodNo < 2) {
            return false;
        }
        return true;
    }

    public int getDefence() {
        return defence;
    }

    public List<InventoryObject> getUsedMaterials(List<InventoryObject> inventory) {
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        List<InventoryObject> usedMaterials = new ArrayList<InventoryObject>();
        boolean sunstoneExists = false;
        for (InventoryObject object : inventory) {
            if (object instanceof Sunstone) {
                sunstoneExists = true;
                break;
            }
        }
        for (InventoryObject object : inventory) {    
            if (object instanceof Wood) {
                if (woodNo < 2) {
                    usedMaterials.add(object);
                    woodNo++;
                }
            }
            if (object instanceof Treasure) {
                if (treasureNo == 0 && keyNo == 0 && !sunstoneExists) {
                    usedMaterials.add(object);
                    treasureNo++;
                }
            }
            if (object instanceof Key) {
                if (keyNo == 0 && treasureNo == 0 && !sunstoneExists) {
                    usedMaterials.add(object);
                    keyNo++;
                }
            }
        }
        return usedMaterials;
    }
}