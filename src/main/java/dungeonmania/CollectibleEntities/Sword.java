package dungeonmania.CollectibleEntities;

import org.json.JSONObject;

public class Sword extends InventoryObject implements Weapon, Durability, MapCollectible {
    private static int attackDamage;
    private int durability;

    public Sword(String id, int attackDamage, int durability) {
        super(id);
        Sword.attackDamage = attackDamage;
        this.durability = durability;
    }

    @Override
    public boolean deteriorate() {
        durability -= 1;
        return durability <= 0;
    }
    @Override
    public double getModifier() {
        return attackDamage;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("durability", durability);
        return newJSON;
    }
}
