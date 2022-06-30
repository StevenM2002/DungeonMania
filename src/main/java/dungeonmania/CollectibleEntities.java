package dungeonmania;

import dungeonmania.util.Position;

public abstract class CollectibleEntities extends Entities {
    private InventoryObject object;

    public CollectibleEntities(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
        this.object = new InventoryObject(id, position, isInteractable);
    }

    public InventoryObject getObject() {
        return object;
    }
}
