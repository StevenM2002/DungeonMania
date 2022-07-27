package dungeonmania;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.CollectibleEntities.Bomb;
import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.ZombieToastSpawner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.IllegalArgumentException;

import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.Collisions.CollisionManager;
import dungeonmania.Goals.GoalManager;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.Goals.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;




public class DungeonManiaController {
    private static DungeonManiaController dmc;
    private List<Entity> allEntities;
    private int currentDungeonID = 0;
    private String dungeonName;
    private Goal goal;
    private JSONObject config;
    private BattleManager battleManager;
    private DungeonSaver dungeonSaver;
    private int currTick;

    private void setConfig(JSONObject config) {
        this.config = config;
    }

    /**
     * Singleton pattern for thread safe static dmc
     * @return
     */
    public static synchronized DungeonManiaController getDmc() {
        return dmc;
    }

    private String getDungeonID() {
        return Integer.toString(currentDungeonID);
    }

    public BattleManager getBattleManager() {
        return battleManager;
    }

    public int getCurrTick() {
        return currTick;
    }

    public boolean hasZombies() {
        for (Entity entity : getDmc().getAllEntities()) {
            if (entity instanceof ZombieToast) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the player, otherwise throws null
     * @return
     */
    public Player getPlayer() {
        return (Player) getDmc().getAllEntities().stream()
            .filter(x->x.getClass().getSimpleName().startsWith("Player"))
            .findFirst().orElse(null);
    }
    public JSONObject getConfig() {
        return config;
    }

    public int getConfigValue(String key) {
        return config.getInt(key);
    }


    public List<Entity> getAllEntities() {
        return allEntities;
    }

    public void removeFromEntities(Entity entity) {
        allEntities.remove(entity);
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }
    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }    
    
    
    private void initDmc(String dungeonName) {
        int newDungeonId = currentDungeonID;
        currentDungeonID++;
        dmc = this;
        dmc.currentDungeonID = newDungeonId;
        allEntities = new ArrayList<>();
        dmc.battleManager = new BattleManager();
        dmc.currTick = 0;
        dmc.dungeonName = dungeonName;
    }
    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        // Initialising the new dungeon
        EntityFactory.setCurrentEntityID(0);
        initDmc(dungeonName);        
        JSONObject dungeon = null;
        JSONObject config = null;
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/"+dungeonName+".json"));
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException("Could not find dungeon file \""+dungeonName+"\"");
        } try {
            config = new JSONObject(FileLoader.loadResourceFile("/configs/"+configName+".json"));
            getDmc().setConfig(config);
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException("Could not find config file \""+configName+"\"");
        }
        loadEntities(dungeon.optJSONArray("entities"));

        getDmc().goal = GoalManager.loadGoals(dungeon.optJSONObject("goal-condition"), config, battleManager);        
        PortalMatcher.configurePortals(allEntities);
        getDmc().dungeonSaver = new DungeonSaver(dungeon, config, getDmc(), dungeonName, getDmc().currentDungeonID);
        return getDungeonResponseModel();
    }


    // Loads entities from a json array. loads the player first
    private void loadEntities(JSONArray entities) {
        // creating the player
        for (int i = 0; i < entities.length(); i++) {
            if (entities.getJSONObject(i).getString("type").equals("player")) {
                EntityFactory.createEntity(entities.getJSONObject(i));
            }
        }
        //creating everything else
        for (int i = 0; i < entities.length(); i++) {
            if (!entities.getJSONObject(i).getString("type").equals("player")) {
                EntityFactory.createEntity(entities.getJSONObject(i));
            }
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        ArrayList<EntityResponse> entityList = new ArrayList<>();
        for (Entity e : getDmc().getAllEntities()) {
            entityList.add(e.getEntityResponse());
        }
        ArrayList<BattleResponse> battleList = (ArrayList<BattleResponse>) getDmc().getBattleManager().getBattleList();
        // creating inventory object list
        // NOTE: checks that player is still alive
        String goals = "dead";
        List<ItemResponse> inventoryList = new ArrayList<>();
        List<String> buildablesList = new ArrayList<>();
        if (getPlayer() != null) {
            goals = getDmc().goal.getTypeString(getPlayer(), getDmc().getAllEntities());
            if (getDmc().getAllEntities().stream().anyMatch(e -> e instanceof Player)) {
                for (InventoryObject i : getPlayer().getInventory()) {
                    inventoryList.add(i.getItemResponse());
                }
            }
            buildablesList = CraftingManager.getBuildables(getPlayer().getInventory(), hasZombies());
        }
        return new DungeonResponse(
            getDungeonID(), 
            getDmc().dungeonName, 
            entityList, 
            inventoryList, 
            battleList, 
            buildablesList,
            goals
        );
    }
    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        var item = getPlayer().getInventory().stream().filter(it -> it.getId().equals(itemUsedId)).findFirst().orElse(null);
        if (item == null) throw new InvalidActionException("Gimme something normal");
        if (!(item instanceof Bomb) && !(item instanceof Potion)) throw new IllegalArgumentException("Not usable");
        getPlayer().queuePotion(itemUsedId);
        doSharedTick();
        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        getPlayer().move(movementDirection);
        // if player dies
        if (getPlayer() == null) {
            return getDungeonResponseModel();
        }
        doSharedTick();
        return getDungeonResponseModel();
    }
    
    private void doSharedTick() {
        currTick++;
        getPlayer().doPotionTick();

        // move all entities
        for (MovingEntity e : getDmc().getAllEntities().stream()
            .filter(entity -> (entity instanceof MovingEntity))
            .map(entity -> (MovingEntity) entity)
            .collect(Collectors.toList())
        ) {
            e.doTickMovement();
        }
        if (getDmc().getPlayer() == null) return; // if player is killed
        doSharedSpawn();
        CollisionManager.deactivateSwitches();
        goal.hasCompleted(getDmc().getPlayer(), getDmc().getAllEntities());
        dungeonSaver.storeCurrentTick(getDmc());
    }

    /**
     * Runs the spawn function for all spawners, including the SpiderSpawner and ZombieToastSpawners
     */
    private void doSharedSpawn() {
        SpiderSpawner.doSpiderSpawn(currTick);
        if (getDmc().getAllEntities().stream().anyMatch(e -> e instanceof ZombieToastSpawner)) {
            for (ZombieToastSpawner z : getDmc().getAllEntities().stream().filter(e -> e instanceof ZombieToastSpawner).map(x->(ZombieToastSpawner) x).collect(Collectors.toList())) {
                z.spawn(getDmc().currTick);
            }
        }
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        CraftingManager.craft(buildable, getPlayer().getInventory(), hasZombies());
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        var toInteractWith = allEntities.stream().filter(entity -> entity.getId().equals(entityId)).findFirst().get();
        if (toInteractWith == null || !(toInteractWith instanceof Interactable)) throw new IllegalArgumentException("Entity cannot be found with specified Id");
        ((Interactable) toInteractWith).interact(getPlayer());

        doSharedSpawn();
        doSharedTick();
        return getDungeonResponseModel();
    }

    public static Stream<EntityResponse> getEntitiesStream(DungeonResponse res, String type) {
        return res.getEntities().stream().filter(it -> it.getType().startsWith(type));
    }

    public static int countEntityOfType(DungeonResponse res, String type) {
        return getEntities(res, type).size();
    }

    public static Optional<EntityResponse> getPlayer(DungeonResponse res) {
        return getEntitiesStream(res, "player").findFirst();
    }

    public static List<EntityResponse> getEntities(DungeonResponse res, String type) {
        return getEntitiesStream(res, type).collect(Collectors.toList());
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        getDmc().dungeonSaver.saveToFile(name);
        return getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        JSONObject loadedDungeon = null;
        try {
            loadedDungeon = new JSONObject(FileLoader.loadResourceFile("SavedGames/"+name+".json"));
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException("Could not find saved game with name: "+name);
        }
        loadGameFromJSON(loadedDungeon);
        return getDungeonResponseModel();
    }

    public static void loadGameFromJSON(JSONObject savedDungeon) {
        loadGameFromJSON(savedDungeon, savedDungeon.getJSONArray("ticks").length() - 1);
    }

    /**
     * @precondition assumes tick is less than or equal to the total number of 
     * ticks in savedDungeon
     * @param savedDungeon
     * @param tick
     */
    private static void loadGameFromJSON(JSONObject savedDungeon, int tick) {
        dmc.config = savedDungeon.getJSONObject("config");
        dmc.dungeonName = savedDungeon.getString("dungeonName");
        dmc.currentDungeonID = savedDungeon.getInt("currentDungeonID");
        dmc.currTick = tick;
        JSONObject currentTick = savedDungeon.getJSONArray("ticks").getJSONObject(tick);
        EntityFactory.setCurrentEntityID(currentTick.getInt("currentEntityID"));
        CraftingManager.setIDCounter(currentTick.getInt("currentCraftingID"));

        // setting the current potion in PotionManager
        JSONObject currPotion = currentTick.getJSONObject("currPotion");
        if (currPotion.getString("name").equals("none")) {
            PotionManager.setCurrPotion(null);
        } else if (currPotion.getString("name").equals("InvisibilityPotion")) {
            PotionManager.setCurrPotion(new InvisibilityPotion(currPotion.getString("id"), dmc.config.getInt("invisibility_potion_duration")));
        } else if (currPotion.getString("name").equals("InvincibilityPotion")) {
            PotionManager.setCurrPotion(new InvincibilityPotion(currPotion.getString("id"), dmc.config.getInt("invincibility_potion_duration")));
        }

        // setting the correct potion queue
        Queue<Potion> potionQueue= new LinkedBlockingQueue<>();
        for (int i = 0; i < currentTick.getJSONArray("potionQueue").length(); i++) {
            JSONObject currQueuedPotion = currentTick.getJSONArray("potionQueue").getJSONObject(i);
            if (currQueuedPotion.getString("name").equals("InvisibilityPotion")) {
                potionQueue.add(new InvisibilityPotion(currQueuedPotion.getString("id"), dmc.config.getInt("invisibility_potion_duration")));
            } else if (currQueuedPotion.getString("name").equals("InvincibilityPotion")) {
                potionQueue.add(new InvincibilityPotion(currQueuedPotion.getString("id"), dmc.config.getInt("invincibility_potion_duration")));
            }
        }
        PotionManager.setPotionQueue(potionQueue);

        dmc.allEntities = new ArrayList<>();
        // Loading the entities. Loads the player first (to avoid null pointer errors with subscriptions)
        for (int i = 0; i < currentTick.getJSONArray("entities").length(); i++) {
            if (currentTick.getJSONArray("entities").getJSONObject(i).getString("type").equals("player")) {
                EntityFactory.createEntity(currentTick.getJSONArray("entities").getJSONObject(i));
            }
        }
        // loading all the other entites
        for (int i = 0; i < currentTick.getJSONArray("entities").length(); i++) {
            if (!currentTick.getJSONArray("entities").getJSONObject(i).getString("type").equals("player")) {
                EntityFactory.createEntity(currentTick.getJSONArray("entities").getJSONObject(i));
            }

        }

        //loading the battles
        dmc.battleManager = new BattleManager(currentTick.getJSONArray("battleList"));

        dmc.goal = GoalManager.loadGoals(savedDungeon.getJSONObject("goal-condition"), dmc.config, dmc.battleManager);
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return new ArrayList<>();
    }


}
