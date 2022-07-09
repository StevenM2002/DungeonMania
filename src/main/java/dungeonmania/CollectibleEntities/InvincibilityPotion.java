package dungeonmania.CollectibleEntities;


public class InvincibilityPotion extends InventoryObject implements Potion, MapCollectible {
    private static int duration;

    public InvincibilityPotion(String id, int duration) {
        super(id);
        this.duration = duration;
    }

    @Override
    public void deplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getDurationEffect() {
        return duration;
    }
}
