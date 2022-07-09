package dungeonmania.CollectibleEntities;


public class Key extends InventoryObject implements MapCollectible {
    private int key;

    public int getKey() {
        return key;
    }

    public Key(String id, int key) {
        super(id);
        this.key = key;
    }
}