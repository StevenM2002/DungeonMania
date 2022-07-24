package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.Buildable;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.exceptions.InvalidActionException;
import static dungeonmania.DungeonManiaController.getDmc;

public class CraftingManager {
    private static int IDCounter = 0;
    private static String getNewCraftingID() {
        String newID = "c"+IDCounter;
        IDCounter++;
        return newID;
    }
    /**
     * 
     * @param type
     */
    public static void craft(String type, List<InventoryObject> inventory) 
        throws 
        IllegalArgumentException, 
        InvalidActionException 
    {
        Buildable newObject;
        if (type.equals("bow")) {
            newObject = new Bow(getNewCraftingID(), 2, getDmc().getConfigValue("bow_durability"));
        } else if (type.equals("shield")) {
            newObject = new Shield(getNewCraftingID(), getDmc().getConfigValue("shield_defence"), getDmc().getConfigValue("shield_durability"));
        } else {
            throw new IllegalArgumentException("Cannot build item of type: "+type);
        }
        if (!newObject.canCraft(inventory)) {
            throw new InvalidActionException("Oops! you do not have enough materials to craft a "+type);
        }
        for (InventoryObject item : newObject.getUsedMaterials(inventory)) {
            inventory.remove(item);
        }
        inventory.add((InventoryObject) newObject);
    }
    /**
     * returns a list of buildable objects based on the given inventory
     * @param inventory
     * @return
     */
    public static List<String> getBuildables(List<InventoryObject> inventory) {
        ArrayList<String> buildables = new ArrayList<>();
        Bow bow = new Bow("0", 420, 69);
        Shield shield = new Shield("0", 420, 69);
        if (bow.canCraft(inventory)) {
            buildables.add("bow");
        }
        if (shield.canCraft(inventory)) {
            buildables.add("shield");
        }
        return buildables;
    }
}
