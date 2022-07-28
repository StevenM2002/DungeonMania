package dungeonmania.CollectibleEntities;

import org.json.JSONObject;

import dungeonmania.response.models.ItemResponse;
import static dungeonmania.util.UtilityFunctions.camelToSnake;


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
            camelToSnake(this.getClass().getSimpleName())
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

    public JSONObject toJSON() {
        JSONObject newJSON = new JSONObject();
        newJSON.put("id", id);
        newJSON.put("type", camelToSnake(this.getClass().getSimpleName()));
        return newJSON;
    }
}
