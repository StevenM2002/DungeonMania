package dungeonmania;

import dungeonmania.CollectibleEntities.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Position;
import static dungeonmania.DungeonManiaController.getDmc;



import org.json.JSONObject;


public class EntityFactory {
    private static int currentEntityID = 0;
    private static String getNewEntityID() {
        String newID = Integer.toString(currentEntityID);
        currentEntityID += 1;
        return newID;
    }
    public static void setCurrentEntityID(int newCurrentEntityID) {
        currentEntityID = newCurrentEntityID;
    }

    /**
     * creates an entity with a new unique id generated by the factory
     * @param type
     * @param position
     * @param extraInfo
     * @return
     */
    public static Entity createEntity(String type, Position position, JSONObject extraInfo) {
        String id = getNewEntityID();
        return createEntity(id, type, position, extraInfo);
    }

    /**
     * Creates an entity with a unique id and returns it
     * @param id
     * @param type
     * @param position
     * @param extraInfo - used when creating portals, keys and doors, otherwise leave null
     */
    public static Entity createEntity(String id, String type, Position position, JSONObject extraInfo) {
        JSONObject config = getDmc().getConfig();
        Entity newEntity = null;
        switch (type) {
            case "player":
                newEntity = new Player(id, position, config.getInt("player_health"), config.getInt("player_attack"));
                break;
            case "wall":
                newEntity = new Wall(id, position);
                break;
            case "exit":
                newEntity = new Exit(id, position);
                break;
            case "boulder":
                newEntity = new Boulder(id, position);
                break;
            case "switch":
                newEntity = new FloorSwitch(id, position);
                break;
            case "door":
                newEntity = new Door(id, position, extraInfo.getInt("key"));
                break;
            case "portal":
                newEntity = new Portal(id, position, extraInfo.getString("colour"));
                break;
            case "zombie_toast_spawner":
                newEntity = new ZombieToastSpawner(id, position, config.getInt("zombie_spawn_rate"));
                break;
            case "spider":
                newEntity = new Spider(id, position, config.getInt("spider_health"), config.getInt("spider_attack"));
                break;
            case "zombie_toast":
                newEntity = new ZombieToast(id, position, config.getInt("zombie_health"), config.getInt("zombie_attack"));
                getDmc().getPlayer().subscribe((PlayerListener) newEntity);
                break;
            case "mercenary":
                newEntity = new Mercenary(id, position, config.getInt("mercenary_health"), config.getInt("mercenary_attack"));
                getDmc().getPlayer().subscribe((PlayerListener) newEntity);
                break;
            case "hydra":
                newEntity = new Hydra(id, position, config.getInt("hydra_health"), config.getInt("hydra_attack"), 
                    config.getDouble("hydra_health_increase_rate"), config.getDouble("hydra_health_increase_amount")
                );
                getDmc().getPlayer().subscribe((PlayerListener) newEntity);
                break;
            case "treasure":
                newEntity = new CollectibleEntity(id, position, new Treasure(id));
                break;
            case "key":
                newEntity = new CollectibleEntity(id, position, new Key(id, extraInfo.getInt("key")));
                break;
            case "invincibility_potion":
                newEntity = new CollectibleEntity(id, position, new InvincibilityPotion(id, config.getInt("invincibility_potion_duration")));
                break;
            case "invisibility_potion":
                newEntity = new CollectibleEntity(id, position, new InvisibilityPotion(id, config.getInt("invisibility_potion_duration")));
                break;
            case "wood":
                newEntity = new CollectibleEntity(id, position, new Wood(id));
                break;
            case "arrow":
                newEntity = new CollectibleEntity(id, position, new Arrow(id));
                break;
            case "bomb":
                newEntity = new CollectibleEntity(id, position, new Bomb(id, config.getInt("bomb_radius")));
                break;
            case "sword":
                newEntity = new CollectibleEntity(id, position, new Sword(id, config.getInt("sword_attack"), config.getInt("sword_durability")));
                break;
            case "sun_stone":
                newEntity = new CollectibleEntity(id, position, new Sunstone(id));
                break;
            case "active_bomb":
                newEntity = new ActiveBomb(id, position, false);
                break;
        }
        if (newEntity != null) {
            getDmc().getAllEntities().add(newEntity);
        } else {
            System.err.println("error: unable to create new entity of type: "+type);
        }
        return newEntity;
    }
    public static void createEntity(JSONObject jsonEntity) {
        createEntity(jsonEntity.getString("type"), getPositionOfEntity(jsonEntity), jsonEntity);
    }
    private static Position getPositionOfEntity(JSONObject entity) {
        return new Position(entity.getInt("x"), entity.getInt("y"));
    }
}
