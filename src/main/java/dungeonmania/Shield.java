package dungeonmania;

import dungeonmania.util.Position;

public class Shield extends InventoryObject implements Buildable, Durability {
    private int defence;
    private int durability;

    public Shield(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    @Override
    public double deteriorate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBattleUses() {
        // TODO Auto-generated method stub
        return 0;
    }
}