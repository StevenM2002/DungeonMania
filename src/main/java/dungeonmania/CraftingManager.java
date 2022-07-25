package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.Buildable;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.MidnightArmor;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sceptre;
import dungeonmania.exceptions.InvalidActionException;
import static dungeonmania.DungeonManiaController.getDmc;

public class CraftingManager {
    private static int IDCounter = 0;
    public static int getIDCounter() {
        return IDCounter;
    }

    private static String getNewCraftingID() {
        String newID = "c"+IDCounter;
        IDCounter++;
        return newID;
    }
    /**
     * 
     * @param type
     */
    public static void craft(String type, List<InventoryObject> inventory, boolean hasZombies) 
        throws 
        IllegalArgumentException, 
        InvalidActionException 
    {
        Buildable newObject;
        if (type.equals("bow")) {
            newObject = new Bow(getNewCraftingID(), 2, getDmc().getConfigValue("bow_durability"));
        } else if (type.equals("shield")) {
            newObject = new Shield(getNewCraftingID(), getDmc().getConfigValue("shield_defence"), getDmc().getConfigValue("shield_durability"));
        } else if (type.equals("sceptre")) {
            newObject = new Sceptre(getNewCraftingID(), getDmc().getConfigValue("mind_control_duration"));
        } else if (type.equals("midnight_armour")) {
            newObject = new MidnightArmor(getNewCraftingID(), getDmc().getConfigValue("midnight_armour_defence"), getDmc().getConfigValue("midnight_armour_attack"));
        } else {
            throw new IllegalArgumentException("Cannot build item of type: "+type);
        }
        if (!newObject.canCraft(inventory, hasZombies)) {
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
    public static List<String> getBuildables(List<InventoryObject> inventory, boolean hasZombies) {
        ArrayList<String> buildables = new ArrayList<>();
        Bow bow = new Bow("0", 420, 69);
        Shield shield = new Shield("0", 420, 69);
        Sceptre sceptre = new Sceptre("0", 81237);
        MidnightArmor midnightArmor = new MidnightArmor("0", 1, 47189724);
        if (bow.canCraft(inventory, hasZombies)) {
            buildables.add("bow");
        }
        if (shield.canCraft(inventory, hasZombies)) {
            buildables.add("shield");
        }
        if (sceptre.canCraft(inventory, hasZombies)) {
            buildables.add("sceptre");
        }
        if (midnightArmor.canCraft(inventory, hasZombies)) {
            buildables.add("midnight_armour");
        }
        return buildables;
    }
}
