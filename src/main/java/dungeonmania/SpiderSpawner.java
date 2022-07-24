package dungeonmania;

import dungeonmania.util.Position;

import java.util.Random;
import static dungeonmania.DungeonManiaController.getDmc;
public class SpiderSpawner {
    private static final int SPAWNING_RADIUS = 10;


    public static Position getSpiderPositionSpawn() {
        Random rand = new Random();
        int x = rand.nextInt(SPAWNING_RADIUS*2) - SPAWNING_RADIUS;
        int y = rand.nextInt(SPAWNING_RADIUS*2) - SPAWNING_RADIUS;
        return new Position(x, y);
    }

    /**
     * if the current tick is the spiders new spawn rate, spawn a new spider
     * @param currTick
     */
    public static void doSpiderSpawn(int currTick) {
        int spawnRate = getDmc().getConfigValue("spider_spawn_rate");
        if (spawnRate == 0 || currTick % spawnRate != 0) return;
        EntityFactory.createEntity("spider", getSpiderPositionSpawn(), null);
    }
}
