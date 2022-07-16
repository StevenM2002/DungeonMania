package dungeonmania;

import dungeonmania.CollectibleEntities.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Position;



import org.json.JSONObject;

import java.util.List;

public class EntityFactory {
    public static Entity createEntity(String id, JSONObject jsonEntity, JSONObject config) {
        Position position = getPositionOfEntity(jsonEntity);
        switch (jsonEntity.getString("type")) {
            case "player":
                return new Player(id, position, config.getInt("player_health"), config.getInt("player_attack"));
            case "wall":
                return new Wall(id, position);
            case "exit":
                return new Exit(id, position);
            case "boulder":
                return new Boulder(id, position);
            case "switch":
                return new FloorSwitch(id, position);
            case "door":
                return new Door(id, position, jsonEntity.getInt("key"));
            case "portal":
                return new Portal(id, position, jsonEntity.getString("colour"));
            case "zombie_toast_spawner":
                return new ZombieToastSpawner(id, position, config.getInt("zombie_spawn_rate"));
            case "spider":
                return new Spider(id, position, config.getInt("spider_health"), config.getInt("spider_attack"));
            case "zombie_toast":
                return new ZombieToast(id, position, config.getInt("zombie_health"), config.getInt("zombie_attack"));
            case "mercenary":
                return new Mercenary(id, position, config.getInt("mercenary_health"), config.getInt("mercenary_attack"));
            case "treasure":
                return new CollectibleEntity(id, position, new Treasure(id));
            case "key":
                return new CollectibleEntity(id, position, new Key(id, jsonEntity.getInt("key")));
            case "invincibility_potion":
                return new CollectibleEntity(id, position, new InvincibilityPotion(id, config.getInt("invincibility_potion_duration")));
            case "invisibility_potion":
                return new CollectibleEntity(id, position, new InvisibilityPotion(id, config.getInt("invisibility_potion_duration")));
            case "wood":
                return new CollectibleEntity(id, position, new Wood(id));
            case "arrow":
                return new CollectibleEntity(id, position, new Arrow(id));
            case "bomb":
                return new CollectibleEntity(id, position, new Bomb(id, config.getInt("bomb_radius")));
            case "sword":
                return new CollectibleEntity(id, position, new Sword(id, config.getInt("sword_attack"), config.getInt("sword_durability")));
        }
        return null;
    }

    private static Position getPositionOfEntity(JSONObject entity) {
        return new Position(entity.getInt("x"), entity.getInt("y"));
    }
}   
