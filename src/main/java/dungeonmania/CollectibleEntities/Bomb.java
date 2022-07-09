package dungeonmania.CollectibleEntities;


public class Bomb extends InventoryObject implements MapCollectible {
    private static int radius;
    public Bomb(String id, int radius) {
        super(id);
        this.radius = radius;
    }

    // if next to active switch, destroy all non-player entities in range on the
    // same tick it was placed.
    public void detonate() {

    }
}
