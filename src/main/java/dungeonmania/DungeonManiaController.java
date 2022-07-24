package dungeonmania;

import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.CollectibleEntities.Bomb;
import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.ZombieToastSpawner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.IllegalArgumentException;

import dungeonmania.CollectibleEntities.InventoryObject;
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
    private int currTick;

    private void setConfig(JSONObject config) {
        this.config = config;
    }

    /**
     * Singleton pattern for thread safe static dmc
     * @return
     */
    public static DungeonManiaController getDmc() {
        return dmc;
    }

    private String getDungeonID() {
        return Integer.toString(currentDungeonID);
    }

    public BattleManager getBattleManager() {
        return battleManager;
    }

    /**
     * gets the player, otherwise throws null
     * @return
     */
    public Player getPlayer() {
        return (Player) allEntities.stream()
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
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find dungeon file \""+dungeonName+"\"");
        } try {
            config = new JSONObject(FileLoader.loadResourceFile("/configs/"+configName+".json"));
            getDmc().setConfig(config);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find config file \""+configName+"\"");
        }
        loadEntities(dungeon.optJSONArray("entities"), config);

        getDmc().goal = GoalManager.loadGoals(dungeon.getJSONObject("goal-condition"), config, battleManager);        
        PortalMatcher.configurePortals(allEntities);
        allEntities.stream().filter(it -> it instanceof Mercenary).forEach(it -> {
            if (it instanceof PlayerListener) {
                PlayerListener casted = (PlayerListener) it;
                getPlayer().subscribe(casted);
            }
        });
        allEntities.stream().filter(it -> it instanceof ZombieToast).forEach(it -> {
            if (it instanceof PlayerListener) {
                PlayerListener casted = (PlayerListener) it;
                getPlayer().subscribe(casted);
            }
        });
        this.currTick = 0;
        return getDungeonResponseModel();
    }



    private void loadEntities(JSONArray entities, JSONObject config) {
        for (int i = 0; i < entities.length(); i++) {
            EntityFactory.createEntity(entities.getJSONObject(i));
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        ArrayList<EntityResponse> entityList = new ArrayList<>();
        for (Entity e : allEntities) {
            entityList.add(e.getEntityResponse());
        }
        ArrayList<BattleResponse> battleList = (ArrayList<BattleResponse>) battleManager.getBattleList();        
        // creating inventory object list
        // NOTE: checks that player is still alive
        String goals = "dead";
        List<ItemResponse> inventoryList = new ArrayList<>();
        List<String> buildablesList = new ArrayList<>();
        if (getPlayer() != null) {
            goals = goal.getTypeString(getPlayer(), allEntities);
            if (allEntities.stream().anyMatch(e -> e instanceof Player)) {
                for (InventoryObject i : getPlayer().getInventory()) {
                    inventoryList.add(i.getItemResponse());
                }
            }
            buildablesList = CraftingManager.getBuildables(getPlayer().getInventory());
        }
        return new DungeonResponse(
            getDungeonID(), 
            dungeonName, 
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
        doSharedSpawn();
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
        doSharedSpawn();
        doSharedTick();
        return getDungeonResponseModel();
    }
    
    private void doSharedTick() {
        currTick++;
        getPlayer().doPotionTick();
        for (MovingEntity e : getDmc().getAllEntities().stream()
            .filter(entity -> (entity instanceof MovingEntity))
            .map(entity -> (MovingEntity) entity).collect(Collectors.toList())
        ) {
            e.doTickMovement();
        }
        CollisionManager.deactivateSwitches();
        if (getDmc().getPlayer() == null) return; // if player is killed
        goal.hasCompleted(getDmc().getPlayer(), getDmc().getAllEntities());
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
        CraftingManager.craft(buildable, getPlayer().getInventory());
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
        return null;
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return new ArrayList<>();
    }

}
