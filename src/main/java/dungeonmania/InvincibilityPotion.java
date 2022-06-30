package dungeonmania;

import dungeonmania.util.Position;

public class InvincibilityPotion extends InventoryObject {
    private int durability;

    public InvincibilityPotion(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}
