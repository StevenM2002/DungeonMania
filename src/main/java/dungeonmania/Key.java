package dungeonmania;

import dungeonmania.util.Position;

public class Key extends InventoryObject {
    private Door door;

    public Key(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door newDoor) {
        this.door = newDoor;
    }
}