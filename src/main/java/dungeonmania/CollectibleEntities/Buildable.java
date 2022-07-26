package dungeonmania.CollectibleEntities;

import java.util.List;

public interface Buildable {
    /**
     * returns true if the craftable object can be crafted
     * @return
     */
    public boolean canCraft(List<InventoryObject> inventory, boolean hasZombies);
    public List<InventoryObject> getUsedMaterials(List<InventoryObject> inventory);
}
