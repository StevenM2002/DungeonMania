package dungeonmania;

import dungeonmania.util.Position;

import java.util.Random;

public class SpiderSpawning {
    private static final int SPAWNING_RADIUS = 10;

    public Position getSpiderPositionSpawn(int currTick) {
        var spiderSpawnRate = DungeonManiaController.getConfigValue("spider_spawn_rate");

        if (spiderSpawnRate == 0 || currTick % spiderSpawnRate != 0) return null;
        Random rand = new Random();
        int x = rand.nextInt(SPAWNING_RADIUS*2) - SPAWNING_RADIUS;
        int y = rand.nextInt(SPAWNING_RADIUS*2) - SPAWNING_RADIUS;
        return new Position(x, y);
    }
}
