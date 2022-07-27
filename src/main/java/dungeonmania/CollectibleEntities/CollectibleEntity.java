package dungeonmania.CollectibleEntities;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class CollectibleEntity extends Entity implements MapCollectible {
    private MapCollectible collectible;

    public MapCollectible getCollectible() {
        return collectible;
    }

    @Override
    public String getType() {
        return collectible.getClass().getSimpleName();
    }

    public CollectibleEntity(String id, Position position, MapCollectible collectible) {
        super(id, position, false);
        this.collectible = collectible;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        for (String key : ((InventoryObject) collectible).toJSON().keySet()) {
            if (!newJSON.has(key)) {
                newJSON.put(key, ((InventoryObject) collectible).toJSON().get(key));
            }
        }
        return newJSON;
    }
}
