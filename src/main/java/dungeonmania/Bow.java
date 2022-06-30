package dungeonmania;

import dungeonmania.util.Position;

public class Bow extends InventoryObject {
    private int attackDamage;
    private int durability;

    public Bow(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}