package dungeonmania.CollectibleEntities;

import java.util.List;

import java.util.ArrayList;

public class Sceptre extends InventoryObject implements Buildable {
    private int effectDuration;
    public Sceptre(String id, int effectDuration) {
        super(id);
        this.effectDuration = effectDuration;
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory, boolean hasZombies) {
        int arrowNo = 0;
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        int sunstoneNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Arrow) {
                arrowNo += 1;
            }
            if (object instanceof Wood) {
                woodNo += 1;
            }
            if (object instanceof Treasure) {
                treasureNo += 1;
            }
            if (object instanceof Key) {
                keyNo += 1;
            }
            if (object instanceof SunStone) {
                sunstoneNo += 1;
            }
        }
        if ((arrowNo < 2 && woodNo < 1) || (keyNo < 1 && treasureNo < 1 && sunstoneNo < 2) || (sunstoneNo < 1)) {
            return false;
        }
        return true;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    @Override
    public List<InventoryObject> getUsedMaterials(List<InventoryObject> inventory) {
        InventoryObject spareArrow = null;
        int arrowNo = 0;
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        int sunstoneNo = 0;
        List<InventoryObject> usedMaterials = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {    
            if (object instanceof Wood) {
                if (woodNo < 1 && arrowNo < 2) {
                    usedMaterials.add(object);
                    usedMaterials.remove(spareArrow);
                    woodNo++;
                }
            }
            if (object instanceof Arrow) {
                if (woodNo < 1 && arrowNo < 2) {
                    usedMaterials.add(object);
                    spareArrow = object;
                    arrowNo++;
                }
            }
            if (object instanceof Treasure) {
                if (treasureNo == 0 && keyNo == 0) {
                    usedMaterials.add(object);
                    treasureNo++;
                }
            }
            if (object instanceof Key) {
                if (keyNo == 0 && treasureNo == 0) {
                    usedMaterials.add(object);
                    keyNo++;
                }
            }
            if (object instanceof SunStone) {
                if (sunstoneNo == 0) {
                    usedMaterials.add(object);
                    sunstoneNo++;
                }
            }
        }
        return usedMaterials;
    }
}
