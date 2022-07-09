package dungeonmania.CollectibleEntities;

import java.util.List;

public class Bow extends InventoryObject implements Buildable, Weapon, Durability {
    private static int attackDamage;
    private static int durability;

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
    public void craft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
        return false;
    }
}