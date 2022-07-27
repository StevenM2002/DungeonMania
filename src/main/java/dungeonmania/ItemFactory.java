package dungeonmania;

import org.json.JSONObject;

import dungeonmania.CollectibleEntities.*;
import static dungeonmania.DungeonManiaController.getDmc;

public class ItemFactory {

    /**
     * Used in the generation of the players inventory on a loaded game
     * @param type
     * @param id
     * @param extraInfo
     * @return
     */
    public static InventoryObject createItem(String type, String id, JSONObject extraInfo) {
        JSONObject config = getDmc().getConfig();
        InventoryObject newItem = null;
        switch (type) {
            case "arrow":
                newItem = new Arrow(id);
                break;
            case "bomb":
                newItem = new Bomb(id, config.getInt("bomb_radius"));
                break;
            case "bow":
                newItem = new Bow(id, extraInfo.getInt("durability"));
                break;
            case "invincibility_potion":
                newItem = new InvincibilityPotion(id, config.getInt("invincibility_potion_duration"));
                break;
            case "invisibility_potion":
                newItem = new InvisibilityPotion(id, config.getInt("invisibility_potion_duration"));
                break;
            case "key":
                newItem = new Key(id, extraInfo.getInt("key"));
                break;
            case "midnight_armor":
                newItem = new MidnightArmor(id, config.getInt("midnight_armour_defence"), config.getInt("midnight_armour_attack"));
                break;
            case "sceptre":
                newItem = new Sceptre(id, config.getInt("mind_control_duration"));
                break;
            case "shield":
                newItem = new Shield(id, config.getInt("shield_defence"), extraInfo.getInt("durability"));
                break;
            case "sunstone":
                newItem = new Sunstone(id);
                break;
            case "sword":
                newItem = new Sword(id, config.getInt("sword_attack"), extraInfo.getInt("durability"));
                break;
            case "treasure":
                newItem = new Treasure(id);
                break;
            case "wood":
                newItem = new Wood(id);
                break;
        }

        return newItem;
    }
}
