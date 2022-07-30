package dungeonmania.CollectibleEntities;


import dungeonmania.Entity;
import dungeonmania.util.Position;

import java.util.List;

import org.json.JSONObject;

import static dungeonmania.DungeonManiaController.getDmc;

public class Bomb extends InventoryObject implements MapCollectible {
    private static int radius;
    private JSONObject extraInfo;
    public Bomb(String id, int radius, JSONObject extraInfo) {
        super(id);
        this.radius = radius;
        this.extraInfo = extraInfo;
    }

    public JSONObject getExtraInfo() {
        return this.extraInfo;
    }
}
