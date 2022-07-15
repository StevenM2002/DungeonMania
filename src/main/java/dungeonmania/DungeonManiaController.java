package dungeonmania;

import dungeonmania.MovingEntities.Interactable;
import dungeonmania.MovingEntities.MovingEntity;
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
    private static List<Entity> allEntities;
    private int currentEntityID;
    private int currentDungeonID = 0;
    private String currentDungeonName;
    private static JSONObject config;
    private CollisionManager collisionManager;

    public String getDungeonID() {
        return Integer.toString(currentDungeonID);
    }
    private void nextDungeonID() {
        currentDungeonID += 1;
    }

    public static Player getPlayer() {
        return (Player) allEntities.stream().filter(x->x.getClass().getSimpleName().startsWith("Player")).findFirst().get();
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

    public static List<Entity> getAllEntities() {
        return allEntities;
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
        collisionManager = new CollisionManager(this);
        Entity.collisionManager = collisionManager;
        
        JSONObject dungeon = null;
        JSONObject config = null;
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
        //TODO: Create goals, and other config stuff
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
        for (InventoryObject i : getPlayer().getInventory()) {
            inventoryList.add(i.getItemResponse());
        }

        ArrayList<BattleResponse> battleList = (ArrayList<BattleResponse>) Battle.getBattleList();
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
        doSharedTick();
        return null;
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        getPlayer().move(movementDirection);
        doSharedTick();
        return getDungeonResponseModel();
    }
    private void doSharedTick() {
        List<MovingEntity> movingEntities = allEntities.stream().filter(entity -> entity instanceof MovingEntity).map(entity -> (MovingEntity) entity).collect(Collectors.toList());
        movingEntities.forEach(entity -> entity.move(getPlayer()));
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
        // TODO Creating dungeon response
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        if (allEntities.stream().anyMatch(e -> e.getId().equals(entityId))) {
            ((Interactable) allEntities.stream().filter(e -> e.getId().equals(entityId)).collect(Collectors.toList()).get(0)).interact(getPlayer());;
        } else {
            throw new IllegalArgumentException("Entity cannot be found with specified Id");
        }

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
