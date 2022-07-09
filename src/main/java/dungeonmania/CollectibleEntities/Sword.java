package dungeonmania.CollectibleEntities;

public class Sword extends InventoryObject implements Weapon, Durability, MapCollectible {
    private static int attackDamage;
    private static int durability;

    public Sword(String id, int attackDamage, int durability) {
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
}
