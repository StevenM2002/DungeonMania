package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private static int spawn_count = 0;
    private static int spawnRate = DungeonManiaController.getConfigValue("spider_spawn_rate");
    
    public Spider(String id, Position position, double health, double attack) {
        super(id, position, false, health, attack, new CircularMovement());
    }

    private static Position findPosition(List<Entity> allEntities) {
        Random rand = new Random();
        int x = rand.nextInt(10);
        int y = rand.nextInt(10);

        if (allEntities.stream().filter(e -> e instanceof Boulder)
                .anyMatch(e -> e.getPosition().equals(new Position(x, y)))) {
            return null;
        }

        return new Position(x, y);
    }

    public static void spawn(List<Entity> allEntities) {
        Random rand = new Random();
        int rand_spawn = rand.nextInt(spawnRate);
        Position newPosition = findPosition(allEntities);
        while (rand_spawn == 0) {
            if (newPosition != null) {
                JSONObject config = DungeonManiaController.getConfig();
                JSONObject jsonEntity = new JSONObject();
                jsonEntity.put("type", "spider");
                jsonEntity.put("x", newPosition.getX());
                jsonEntity.put("y", newPosition.getY());
                allEntities.add(EntityFactory.createEntity("spider" + spawn_count, jsonEntity, config));
                spawn_count++;
                break;
            }
            newPosition = findPosition(allEntities);
        }
    }
}
