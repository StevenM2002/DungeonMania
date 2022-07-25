package dungeonmania;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.CollectibleEntities.InventoryObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class DungeonSaver {
    private JSONObject savedDungeon;

    public DungeonSaver(JSONObject dungeon, JSONObject config, DungeonManiaController dmc, String dungeonName, int dungeonID) {
        savedDungeon = new JSONObject();
        savedDungeon.put("config", config);
        savedDungeon.put("goal-condition", dungeon.getJSONObject("goal-condition"));
        savedDungeon.put("dungeonName", dungeonName);
        savedDungeon.put("currentDungeonID", dungeonID);
        savedDungeon.put("ticks", new JSONArray());
        storeCurrentTick(dmc);
    }

    public DungeonSaver(JSONObject savedDungeon) {
        this.savedDungeon = savedDungeon;
    }

    /**
     * Saves all ticks in savedDungeon up to tick
     * @param savedDungeon
     * @param tick
     */
    public DungeonSaver(JSONObject savedDungeon, int tick) {
        this.savedDungeon = savedDungeon;
        JSONArray ticks = savedDungeon.getJSONArray("ticks");
        this.savedDungeon.remove("ticks");
        JSONArray newTicks = new JSONArray();
        for (int i = 0; i <= tick && i < ticks.length(); i++) {
            newTicks.put(ticks.getJSONObject(i));
        }
        this.savedDungeon.put("ticks", newTicks);
    }

    /**
     * Stores all the information regarding the current tick in the savedDungeon
     * Stored information includes:
     * @apiNote the current tick
     * @apiNote the current entity id from EntityFactory
     * @apiNote the current id from CraftingManager
     * @apiNote the current potion in Potionmanager
     * @apiNote the potion queue
     * @apiNote the list of all entities converted to JSON. NOTE: the JSON object
     * representing the player stores the inventory
     * @apiNote stores the battleList, converted to JSON
     * @param dmc
     */
    public void storeCurrentTick(DungeonManiaController dmc) {
        JSONObject currTick = new JSONObject();
        currTick.put("tick", dmc.getCurrTick());
        currTick.put("currentEntityID", EntityFactory.getCurrentEntityID());
        currTick.put("currentCraftingID", CraftingManager.getIDCounter());
        
        // doing potions
        JSONObject currentPotion = new JSONObject();
        if (PotionManager.getCurrPotion() == null) {
            currentPotion.put("name", "none");
        } else {
            currentPotion.put("name", PotionManager.getCurrPotion().getName());
            currentPotion.put("id", ((InventoryObject) PotionManager.getCurrPotion()).getId());
        }
        currTick.put("currPotion", currentPotion);
        currTick.put("potionQueue", new JSONArray());
        PotionManager.getPotionQueue()
            .forEach(p->currTick.getJSONArray("potionQueue").put((new JSONObject()).put("potion", p.getName())));
        
        // doing entity list
        currTick.put("entities", new JSONArray());
        for (Entity e : dmc.getAllEntities()) {
            currTick.getJSONArray("entities").put(e.toJSON());
        }

        currTick.put("battleList", dmc.getBattleManager().battleListToJSON());
        savedDungeon.getJSONArray("ticks").put(currTick);
        
    }
    /**
     * Saves the current stored
     */
    public void saveToFile(String fileName) {
        String path = "src/main/resources/SavedGames/"+fileName+".json";
        try (PrintWriter out = new PrintWriter(new FileWriter(path));){
            out.write(savedDungeon.toString());
        } catch (IOException e) {
            System.err.println("File could not be saved");
            e.printStackTrace();
        }
    }
}
