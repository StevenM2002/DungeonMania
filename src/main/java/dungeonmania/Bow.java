package dungeonmania;

import dungeonmania.util.Position;

public class Bow extends InventoryObject implements Buildable, Weapon, Durability {
    private int attackDamage;
    private int durability;

    public Bow(String id, boolean isInteractable) {
        super(id, null, isInteractable);
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
}