package dungeonmania;

import dungeonmania.util.Position;

public class Shield extends InventoryObject {
    private int defence;
    private int durability;

    public Shield(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}