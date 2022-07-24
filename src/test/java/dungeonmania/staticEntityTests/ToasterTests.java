package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getEntities;

/**
 * MAP:
 * [ ] [ ] [ ] [ ] -1
 * [ ] [s] [ ] [ ]  0
 * [ ] [p] [t] [ ]  1
 * [ ] [w] [ ] [ ]  2
 *  0   1   2   3
 */

public class ToasterTests {
    @Test
    @DisplayName("Test spawns zombie each tick")
    public void testSpawnZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testZombieToastSpawner", "c_staticTests_staticConfig");
        assertTrue(getEntities(initDungeonRes, "zombie_toast").size() == 0);
        
        // first tick, one zombie spawns
        DungeonResponse firstTickRes = dmc.tick(Direction.LEFT);
        assertTrue(getEntities(firstTickRes, "zombie_toast").size() == 1);

        DungeonResponse secondTickRes = dmc.tick(Direction.LEFT);
        assertTrue(getEntities(secondTickRes, "zombie_toast").size() == 2);
    }

    @Test
    @DisplayName("Test try to destroy spawner without weapon")
    public void testDestroySpawnerFail() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testZombieToastSpawner", "c_staticTests_staticConfig");
        EntityResponse toaster = getEntities(initRes, "zombie_toast_spawner").stream().findFirst().get();
        assertThrows(InvalidActionException.class,()->dmc.interact(toaster.getId()));

    }

    @Test
    @DisplayName("Test destroy spawner with weapon")
    public void testDestroySpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testZombieToastSpawner", "c_staticTests_staticConfig");
        EntityResponse toaster = getEntities(initRes, "zombie_toast_spawner").stream().findFirst().get();
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(()->dmc.interact(toaster.getId()));
        DungeonResponse postRes = dmc.tick(Direction.DOWN);
        assertTrue(getEntities(postRes, "zombie_toast_spawner").size() == 0);

    }

    @Test
    @DisplayName("Test not adjacent with toaster")
    public void testNotAdjacentSpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testZombieToastSpawner", "c_staticTests_staticConfig");
        EntityResponse toaster = getEntities(initRes, "zombie_toast_spawner").stream().findFirst().get();
        dmc.tick(Direction.UP);
        assertThrows(InvalidActionException.class,()->dmc.interact(toaster.getId()));

    }
    public static void main(String[] args) {
        ToasterTests t = new ToasterTests();
        t.testSpawnZombies();
    }
}
