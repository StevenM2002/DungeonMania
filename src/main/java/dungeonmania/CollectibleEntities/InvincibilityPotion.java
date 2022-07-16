package dungeonmania.CollectibleEntities;


public class InvincibilityPotion extends InventoryObject implements Potion, MapCollectible {
    private int duration;

    public InvincibilityPotion(String id, int duration) {
        super(id);
        this.duration = duration;
    }

    @Override
    public int getDurationEffect() {
        return duration;
    }

    @Override
    public String getName() {
        return "InvincibilityPotion";
    }
}
