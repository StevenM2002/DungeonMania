package dungeonmania;

import dungeonmania.util.Position;

public class Sword extends InventoryObject {
    private int attackDamage;
    private int durability;

    public Sword(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}
