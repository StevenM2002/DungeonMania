package dungeonmania.CollectibleEntities;

import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.UtilityFunctions;

public abstract class InventoryObject {
    private String id;

    public String getId() {
        return id;
    }

    public InventoryObject(String id) {
        this.id = id;
    }

    /**
     * Formats the item into a getItemResponse type
     * @return
     */
    public ItemResponse getItemResponse() {
        return new ItemResponse(
            id, 
            UtilityFunctions.camelToSnake(this.getClass().getSimpleName())
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InventoryObject) {
            InventoryObject inv = (InventoryObject) obj;
            return inv.getId() == this.getId();
        }
        return false;
    }
}
