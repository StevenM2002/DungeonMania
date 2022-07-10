package dungeonmania;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.IllegalArgumentException;

import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class DungeonManiaController {
    private List<Entity> allEntities;
    private List<InventoryObject> inventory;
    private int currentEntityID;
    private int currentDungeonID = 0;
    private String currentDungeonName;

    private String getDungeonID() {
        return Integer.toString(currentDungeonID);
    }
    private void nextDungeonID() {
        currentDungeonID += 1;
    }

    public Player getPlayer() {
        return (Player) allEntities.stream().filter(x->x.getClass().getSimpleName().startsWith("Player")).findFirst().get();
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
        allEntities = new ArrayList<Entity>();
        inventory = new ArrayList<InventoryObject>();
        currentEntityID = 0;
        nextDungeonID();
        currentDungeonName = dungeonName;
        
        JSONObject dungeon = null;
        JSONObject config = null;
        try {
            dungeon = new JSONObject(FileLoader.loadResourceFile("/dungeons/"+dungeonName+".json"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find dungeon file \""+dungeonName+"\"");
        } try {
            config = new JSONObject(FileLoader.loadResourceFile("/configs/"+configName+".json"));
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
        for (InventoryObject i : inventory) {
            inventoryList.add(i.getItemResponse());
        }

        //TODO: add once battles are implemented
        ArrayList<BattleResponse> battleList = new ArrayList<>();
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
        return null;
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        getPlayer().move(movementDirection);
        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException
        if (!buildable.equals("bow") || !buildable.equals("shield")) {
            throw new IllegalArgumentException("You can only construct bows or shields");
        }
        // Checking the amount of materials
        List<InventoryObject> bowMaterial = new ArrayList<InventoryObject>();
        List<InventoryObject> shieldMaterial = new ArrayList<InventoryObject>();
        int arrowNo = 0;
        int woodNo = 0;
        int treasureNo = 0;
        int keyNo = 0;
        for (InventoryObject object : inventory) {
            if (object instanceof Arrow) {
                arrowNo += 1;
                if (arrowNo <= 3) {
                    bowMaterial.add(object);
                }
            }
            if (object instanceof Wood) {
                woodNo += 1;
                if (woodNo == 1) {
                    bowMaterial.add(object);
                    shieldMaterial.add(object);
                }
                if (woodNo == 2) {
                    shieldMaterial.add(object);
                }
            }
            if (object instanceof Treasure) {
                treasureNo += 1;
                if (treasureNo == 1) {
                    shieldMaterial.add(object);
                }
            }
            if (object instanceof Key) {
                keyNo += 1;
                if (keyNo == 1) {
                    shieldMaterial.add(object);
                }
            }
        }
        if (buildable.equals("bow")) {
            // InvalidActionException
            if (arrowNo < 3) {
                throw new InvalidActionException("Not enough arrows");
            }
            if (woodNo < 1) {
                throw new InvalidActionException("Not enough wood");
            }
            // Crafting
            inventory.add(new Bow(String.valueOf(allEntities.size()), true));
            // Removing crafting materials
            for (InventoryObject object : bowMaterial) {
                inventory.remove(object);
            }
        }
        if (buildable.equals("shield")) {
            // InvalidActionException
            if (woodNo < 2) {
                throw new InvalidActionException("Not enough wood");
            }
            if (keyNo < 1 && treasureNo < 1) {
                throw new InvalidActionException("Not enough metal");
            }
            // Crafting
            inventory.add(new Shield(String.valueOf(allEntities.size()), true));
            // Removing crafting materials
            for (InventoryObject object : shieldMaterial) {
                if (!(treasureNo > 0 && object instanceof Key)) { // If the player had a treasure, then don't remove the key
                    inventory.remove(object);
                }
            }
        }
        // TODO Creating dungeon response
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
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

    public static void main(String[] args) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y - 1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            System.out.println(movementTrajectory.get(nextPositionElement));
            System.out.println(getEntities(res, "spider").get(0).getPosition());

            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        }
    }
}
