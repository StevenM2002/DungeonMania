package dungeonmania.CollectibleEntities;

import java.util.List;

public class Shield extends InventoryObject implements Buildable, Durability {
    private static int defence;
    private static int durability;

    public Shield(String id, int defence, int durability) {
        super(id);
        this.defence = defence;
        this.durability = durability;
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

    @Override
    public boolean canCraft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void craft(List<InventoryObject> inventory) {
        // TODO Auto-generated method stub
    }
}