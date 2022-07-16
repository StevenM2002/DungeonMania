package dungeonmania;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class SpiderSpawning {
    private static final int SPAWNING_RADIUS = 6;
    private HashSet<Position> getPositionsToSpawnIn(Player player) {
        HashSet<Position> radiusSquare = new HashSet<>();
        radiusSquare.add(player.getPosition());
        for (int i = 0; i < SPAWNING_RADIUS; i++) {
            ArrayList<Position> toBeAdded = new ArrayList<>();
            for (var square : radiusSquare) {
                toBeAdded.addAll(square.getAdjacentPositions());
            }
            radiusSquare.addAll(toBeAdded);
        }
        return radiusSquare;
    }
    public Position getSpiderPositionSpawn(Player player, int currTick) {
        var spiderSpawnRate = DungeonManiaController.getConfigValue("spider_spawn_rate");

        if (spiderSpawnRate == 0 || currTick % spiderSpawnRate != 0) return null;
        var spawnPlaces = new ArrayList<>(getPositionsToSpawnIn(player));
        Random rand = new Random();
        int randInt = rand.nextInt(spawnPlaces.size());
        return spawnPlaces.get(randInt);
    }
}
