package dungeonmania.CollectibleEntities;

import dungeonmania.exceptions.InvalidActionException;
import java.util.List;

public interface Buildable {
    /**
     * returns true if the craftable object can be crafted
     * @return
     */
    public boolean canCraft(List<InventoryObject> inventory);
    public void craft(List<InventoryObject> inventory) throws IllegalArgumentException, InvalidActionException;
}
