package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    private static int spawnRate;
    public ZombieToastSpawner(String id, Position position, int spawnRate) {
        super(id, position, true);
        this.spawnRate = spawnRate;
    }

    public void spawn() {

    }
}
