package dungeonmania.CollectibleEntities;

import java.util.List;
import java.util.ArrayList;

public class MidnightArmor extends InventoryObject implements Buildable, Weapon{
    private int defence;
    private int attackDamage;
    public MidnightArmor(String id, int defence, int attackDamage) {
        super(id);
        this.defence = defence;
        this.attackDamage = attackDamage;
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory, boolean hasZombies) {
        int sunstoneNo = 0;
        int swordNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Sunstone) {
                sunstoneNo += 1;
            }
            if (object instanceof Sword) {
                swordNo += 1;
            }
        }
        if (sunstoneNo < 1 || swordNo < 1 || hasZombies) {
            return false;
        }
        return true;
    }

    public int getDefence() {
        return defence;
    }

    @Override
    public double getModifier() {
        return attackDamage;
    }

    @Override
    public List<InventoryObject> getUsedMaterials(List<InventoryObject> inventory) {
        int sunstoneCount = 0;
        int swordCount = 0;
        List<InventoryObject> usedMaterials = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {
            if (object instanceof Sword) {
                if (swordCount == 0) {
                    usedMaterials.add(object);
                    swordCount++;
                }
            }
            if (object instanceof Sunstone) {
                if (sunstoneCount == 0) {
                    usedMaterials.add(object);
                    sunstoneCount++;
                }
            }
        }
        return usedMaterials;
    }
}
