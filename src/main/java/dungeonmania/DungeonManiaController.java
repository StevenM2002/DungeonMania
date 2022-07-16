package dungeonmania;

import dungeonmania.CollectibleEntities.Bomb;
import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.MovingEntities.*;
import dungeonmania.util.Position;
import dungeonmania.StaticEntities.ZombieToastSpawner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.IllegalArgumentException;

import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.Collisions.CollisionManager;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DungeonManiaController {
    private SpiderSpawning spiderSpawning;
    private static List<Entity> allEntities;
    private int currentEntityID;
    private int currentDungeonID = 0;
    private String currentDungeonName;
    private static JSONObject config;
    private CollisionManager collisionManager;
    private BattleManager battleManager;
    private int currTick;

    public BattleManager getBattleManager() {
        return battleManager;
    }
    private String getDungeonID() {
        return Integer.toString(currentDungeonID);
    }
    private void nextDungeonID() {
        currentDungeonID += 1;
    }

    public Player getPlayer() {
        return (Player) allEntities.stream().filter(x->(x instanceof Player)).findFirst().get();
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static int getConfigValue(String key) {
        return config.getInt(key);
    }
    /**
     * returns a new id to be assigned to an entity object
     * also iterates the current id to a new value, so each id is unique
     * @return
     */
    private String getNewEntityID() {
        String newID = Integer.toString(currentEntityID);
        currentEntityID += 1;
        return newID;
    }

    public List<Entity> getAllEntities() {
        return allEntities;
    }

    public static void removeFromEntities(Entity entity) {
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

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        // Initialising the new dungeon
        allEntities = new ArrayList<>();
        currentEntityID = 0;
        nextDungeonID();
        currentDungeonName = dungeonName;

        // initialising collisions
        collisionManager = new CollisionManager(this);
        Entity.collisionManager = collisionManager;
        battleManager = new BattleManager(this);
        
        JSONObject dungeon = null;
        JSONObject config = null;

        //TODO: fix this, now its a precondition that loadResourceFile gets a file that exists
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/"+dungeonName+".json"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find dungeon file \""+dungeonName+"\"");
        } try {
            config = new JSONObject(FileLoader.loadResourceFile("/configs/"+configName+".json"));
            DungeonManiaController.config = config;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find config file \""+configName+"\"");
        }
        loadEntities(dungeon.optJSONArray("entities"), config);
        
        PortalMatcher.configurePortals(allEntities);
        Movement.player = getPlayer();
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
        this.spiderSpawning = new SpiderSpawning();
        this.currTick = 0;
        return getDungeonResponseModel();
    }

    private void loadEntities(JSONArray entities, JSONObject config) {
        for (int i = 0; i < entities.length(); i++) {
            JSONObject JSONEntity = entities.getJSONObject(i);
            allEntities.add(EntityFactory.createEntity(getNewEntityID(), JSONEntity, config));
        }
    }



    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        // creating the entity list, TODO: could maybe do with streams
        ArrayList<EntityResponse> entityList = new ArrayList<>();
        for (Entity e : allEntities) {
            entityList.add(e.getEntityResponse());
        }
        
        // creating inventory object list
        ArrayList<ItemResponse> inventoryList = new ArrayList<>();
        if (allEntities.stream().anyMatch(e -> e instanceof Player)) {
            for (InventoryObject i : getPlayer().getInventory()) {
                inventoryList.add(i.getItemResponse());
            }
        }

        ArrayList<BattleResponse> battleList = (ArrayList<BattleResponse>) battleManager.getBattleList();
        //TODO: add once crafting is implemented
        ArrayList<String> buildables = new ArrayList<>();
        //TODO: finish once goals are implemented
        String goals = "";

        return new DungeonResponse(
            getDungeonID(), 
            currentDungeonName, 
            entityList, 
            inventoryList, 
            battleList, 
            buildables, 
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
        return null;
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        getPlayer().move(movementDirection);
        doSharedSpawn();
        doSharedTick();
        return getDungeonResponseModel();
    }
    private void doSharedTick() {
        currTick++;
        getPlayer().doPotionTick();
        List<MovingEntity> movingEntities = allEntities.stream().filter(entity -> entity instanceof MovingEntity).map(entity -> (MovingEntity) entity).collect(Collectors.toList());
        movingEntities.forEach(entity -> entity.doTickMovement());
        Position spiderPosition = spiderSpawning.getSpiderPositionSpawn(getPlayer(), currTick);
        if (spiderPosition != null) allEntities.add(new Spider(getNewEntityID(), spiderPosition, config.getDouble("spider_health"), config.getInt("spider_attack")));
        collisionManager.deactivateSwitches();
    }

    private void doSharedSpawn() {
        if (allEntities.stream().anyMatch(e -> e instanceof ZombieToastSpawner)) {
            allEntities.stream().filter(e -> e instanceof ZombieToastSpawner).map(e -> (ZombieToastSpawner) e).collect(Collectors.toList()).forEach(e -> e.spawn(allEntities, currTick));
        }
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException
        if (!buildable.equals("bow") || !buildable.equals("shield")) {
            throw new IllegalArgumentException("You can only construct bows or shields");
        }
        getPlayer().addCraftItemToInventory(buildable, DungeonManiaController.config, allEntities.size());
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
        return getEntitiesResponse(res, type).size();
    }

    public static Optional<EntityResponse> getPlayerResponse(DungeonResponse res) {
        return getEntitiesStream(res, "player").findFirst();
    }

    public static List<EntityResponse> getEntitiesResponse(DungeonResponse res, String type) {
        return getEntitiesStream(res, type).collect(Collectors.toList());
    }
}
