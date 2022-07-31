package dungeonmania.CollectibleEntities;



import org.json.JSONObject;

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
