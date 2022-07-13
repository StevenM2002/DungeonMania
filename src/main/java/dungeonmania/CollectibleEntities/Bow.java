package dungeonmania.CollectibleEntities;

import dungeonmania.exceptions.InvalidActionException;
import java.util.List;
import java.util.ArrayList;

public class Bow extends InventoryObject implements Buildable, Weapon, Durability {
    private static int attackDamage;
    private int durability;

    public Bow(String id, int attackDamage, int durability) {
        super(id);
        this.attackDamage = attackDamage;
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
    public double buffAttack(double baseAttack) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getModifier() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void craft(List<InventoryObject> inventory) throws IllegalArgumentException, InvalidActionException {
        int arrowNo = 0;
        int woodNo = 0;
        List<InventoryObject> usedMaterial = new ArrayList<InventoryObject>();
        for (InventoryObject object : inventory) {
            if (object instanceof Arrow) {
                arrowNo += 1;
                if (arrowNo <= 3) {
                    usedMaterial.add(object);
                }
            }
            if (object instanceof Wood) {
                woodNo += 1;
                if (woodNo == 1) {
                    usedMaterial.add(object);
                }
            }
        }
        // InvalidActionException
        if (arrowNo < 3) {
            throw new InvalidActionException("Not enough arrows");
        }
        if (woodNo < 1) {
            throw new InvalidActionException("Not enough wood");
        }
        // Crafting
        int newId = Integer.parseInt(super.getId()) + 1; // Can't have the new entity be the same id as this entity
        inventory.add(new Bow(String.valueOf(newId), 2, this.durability));
        // Removing crafting materials
        for (InventoryObject object : usedMaterial) {
            inventory.remove(object);
        }
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
        return true;
    }
}