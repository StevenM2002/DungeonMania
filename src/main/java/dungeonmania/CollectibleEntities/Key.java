package dungeonmania.CollectibleEntities;

import org.json.JSONObject;

public class Key extends InventoryObject implements MapCollectible {
    private int key;

    public int getKey() {
        return key;
    }

    public Key(String id, int key) {
        super(id);
        this.key = key;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("key", key);
        return newJSON;
    }
}