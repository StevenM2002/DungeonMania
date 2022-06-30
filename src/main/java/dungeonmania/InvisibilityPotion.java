package dungeonmania;

import dungeonmania.util.Position;

public class InvisibilityPotion extends InventoryObject {
    private int durability;

    public InvisibilityPotion(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}
