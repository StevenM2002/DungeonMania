package dungeonmania;

import dungeonmania.util.Position;

public class InvincibilityPotion extends InventoryObject implements Potion {
    private int durability;

    public InvincibilityPotion(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    @Override
    public void deplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getDurationEffect() {
        // TODO Auto-generated method stub
        return 0;
    }
}
