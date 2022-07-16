package dungeonmania.CollectibleEntities;


public class InvisibilityPotion extends InventoryObject implements Potion, MapCollectible {
    private  int duration;
    public InvisibilityPotion(String id, int duration) {
        super(id);
        this.duration = duration;
    }

    @Override
    public int getDurationEffect() {
        return duration;
    }

    @Override
    public String getName() {
        return "InvisibilityPotion";
    }
}
