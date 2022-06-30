package dungeonmania;

import dungeonmania.util.Position;

public class Sword extends InventoryObject implements Weapon, Durability {
    private int attackDamage;
    private int durability;

    public Sword(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
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
