package dungeonmania;

import dungeonmania.util.Position;

public class Bomb extends InventoryObject {
    public Bomb(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    // if next to active switch, destroy all non-player entities in range on the
    // same tick it was placed.
    public void detonate() {

    }
}
