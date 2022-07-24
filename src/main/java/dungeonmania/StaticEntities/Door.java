package dungeonmania.StaticEntities;

import org.json.JSONObject;

import dungeonmania.CollectibleEntities.Key;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;

public class Door extends StaticEntity {
    private int key;
    private boolean locked;
    public Door(String id, Position position, int key) {
        super(id, position, false);
        this.key = key;
        this.locked = true;
    }

    public boolean keyMatchesDoor(Key k) {
        return k.getKey() == key;
    }

    public void unlock() {
        locked = false;
    }
    public boolean isLocked() {
        return locked;
    }

    @Override
    public String getDefaultCollision() {
        if (locked) {
            return "Block";
        } 
        return "Pass";
    }
    @Override
    public EntityResponse getEntityResponse() {
        String type;
        if (locked) {
             type = getType();
        } else {
            type = getType() + "_open";
        }
        return new EntityResponse(
                getId(),
                UtilityFunctions.camelToSnake(type),
                getPosition(),
                getIsInteractable());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("key", key);
        return newJSON;
    }
}