package dungeonmania.CollectibleEntities;


public class InvisibilityPotion extends InventoryObject implements Potion, MapCollectible {
    private static int duration;
    public InvisibilityPotion(String id, int duration) {
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

    @Override
    public String getName() {
        return "InvisibilityPotion";
    }
}
