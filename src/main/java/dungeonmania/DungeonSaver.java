package dungeonmania;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DungeonSaver {
    private JSONObject savedDungeon;

    public JSONObject getSavedDungeon() {
        return savedDungeon;
    }

    public DungeonSaver(JSONObject dungeon, JSONObject config, DungeonManiaController dmc, String dungeonName, int dungeonID) {
        savedDungeon = new JSONObject();
        savedDungeon.put("config", config);
        savedDungeon.put("goal-condition", dungeon.getJSONObject("goal-condition"));
        savedDungeon.put("dungeonName", dungeonName);
        savedDungeon.put("currentDungeonID", dungeonID);
        savedDungeon.put("ticks", new JSONArray());
        storeCurrentTick(dmc, null);
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

    public void updateCurrentTick(DungeonManiaController dmc) {
        JSONObject currTick = savedDungeon.getJSONArray("ticks").getJSONObject(savedDungeon.getJSONArray("ticks").length() - 1);
        // doing entity list
        currTick.put("entities", new JSONArray());
        for (Entity e : dmc.getAllEntities()) {
            currTick.getJSONArray("entities").put(e.toJSON());
        }
        savedDungeon.getJSONArray("ticks").remove((savedDungeon.getJSONArray("ticks").length() - 1));
        savedDungeon.getJSONArray("ticks").put(currTick);
        //;
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
    public void storeCurrentTick(DungeonManiaController dmc, Direction moved) {
        JSONObject currTick = new JSONObject();
        currTick.put("tick", dmc.getCurrTick());
        currTick.put("currentEntityID", EntityFactory.getCurrentEntityID());
        currTick.put("currentCraftingID", CraftingManager.getIDCounter());
        if (moved != null) {
            currTick.put("moved", moved.toString());
        }
        
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
        // for each potion in potionqueue, put in a jsonObject containing the 
        // name and id of the potion
        PotionManager.getPotionQueue()
            .forEach(p->currTick.getJSONArray("potionQueue").put(
                (new JSONObject())
                .put("name", p.getName())
                .put("id", ((InventoryObject) p).getId()))
        );
        
        // doing entity list
        currTick.put("entities", new JSONArray());
        for (Entity e : dmc.getAllEntities()) {
            currTick.getJSONArray("entities").put(e.toJSON());
        }

        currTick.put("battleList", dmc.getBattleManager().battleListToJSON());
        savedDungeon.getJSONArray("ticks").put(currTick);
        
    }
    /**
     * Saves the current stored dungeon to a json file in resources/SavedGames
     * if there is no directory resources/SavedGames
     */
    public void saveToFile(String fileName) {
        try {
            String savesPath = getPathForNewFile("", "SavedGames");
            File dir = new File(savesPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String path = savesPath + "/" + fileName + ".json";
            PrintWriter out = new PrintWriter(new FileWriter(path));
            out.write(savedDungeon.toString());
            out.close();
        } catch (IOException | NullPointerException e) {
            System.err.println("File could not be saved");
            e.printStackTrace();
        }
    }


    /**
     * Returns the path of a new file to be created that is relative to resources/.
     * Will add a `/` prefix to path if it's not specified.
     *
     * @precondition the `resources/directory` MUST exist before, otherwise throws NullPointerException
     * @param directory Relative to resources/ will add an implicit `/` prefix if
     *                  not given.
     * @param newFile   file name
     * @return the full path as a string
     * @throws NullPointerException directory does not exist
     */
    private static String getPathForNewFile(String directory, String newFile) throws IOException, NullPointerException {
        if (!directory.startsWith("/"))
            directory = "/" + directory;
        try {
            Path root = Paths.get(FileLoader.class.getResource(directory).toURI());
            return root.toString() + "/" + newFile;
        } catch (URISyntaxException e) {
            throw new FileNotFoundException(directory);
        }
    }
}
