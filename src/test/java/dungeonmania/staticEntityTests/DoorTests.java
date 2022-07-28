package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getEntityFromID;

/** 
 * MAP:
 * [k] [ ] [d] 0
 * [p] [ ] [ ] 1
 * [k] [b] [d] 2
 *  1   2   3
 */
public class DoorTests {
    @Test
    @DisplayName("Test player move into door without key")
    public void testDoorNoKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        Position expectedPlayerPosition = new Position(2, 0);

        // check player doesn't move through door without key
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        DungeonResponse doorPushRes = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPlayerPosition, getPlayer(doorPushRes).get().getPosition());
    }

    @Test
    @DisplayName("Test player move into door with key")
    public void testDoorWithKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        Position expectedPlayerPosition = new Position(3, 0);
        assertTrue(getInventory(initRes, "key").size() == 0);

        // pick up key
        DungeonResponse keyPickupRes = dmc.tick(Direction.UP);
        assertTrue(getInventory(keyPickupRes, "key").size() == 1);
        dmc.tick(Direction.RIGHT);
        DungeonResponse doorPushRes = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPlayerPosition, getPlayer(doorPushRes).get().getPosition());
        assertTrue(getInventory(doorPushRes, "key").size() == 0);
    }
    @Test
    @DisplayName("Test player move into door with wrong key")
    public void testDoorWrongKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        Position expectedPlayerPosition = new Position(2, 0);
        assertTrue(getInventory(initRes, "key").size() == 0);

        // Pick up wrong key
        DungeonResponse keyPickupRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(keyPickupRes, "key").size() == 1);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        DungeonResponse doorPushRes = dmc.tick(Direction.RIGHT);
        assertEquals(expectedPlayerPosition, getPlayer(doorPushRes).get().getPosition());
        assertTrue(getInventory(doorPushRes, "key").size() == 1);
    }
    @Test
    @DisplayName("Test player open two doors")
    public void testOpenTwoDoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        assertTrue(getInventory(initRes, "key").size() == 0);

        // Pick up bottom key
        DungeonResponse bottomKeyPickupRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(bottomKeyPickupRes, "key").size() == 1);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // open bottom door
        DungeonResponse bottomDoorPushRes = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), getPlayer(bottomDoorPushRes).get().getPosition());
        assertTrue(getInventory(bottomDoorPushRes, "key").size() == 0);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        DungeonResponse topKeyPickupRes = dmc.tick(Direction.UP);
        assertTrue(getInventory(topKeyPickupRes, "key").size() == 1);
        
        dmc.tick(Direction.RIGHT);
        DungeonResponse openTopDoorRes = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 0), getPlayer(openTopDoorRes).get().getPosition());
        assertTrue(getInventory(openTopDoorRes, "key").size() == 0);
    }
    @Test
    @DisplayName("Test push boulder into closed door")
    public void testPushBoulderClosedDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        EntityResponse initBoulder = getEntities(initRes, "boulder").stream().findFirst().get();

        // Push boulder
        dmc.tick(Direction.DOWN);
        DungeonResponse boulderPushRes = dmc.tick(Direction.RIGHT);

        // test boulder did not move
        assertEquals(new Position(2, 2), getEntityFromID(boulderPushRes, initBoulder.getId()).get().getPosition());
    }
    @Test
    @DisplayName("Test push boulder through open door")
    public void testPushBoulderOpenDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testDoors", "c_staticTests_staticConfig");
        EntityResponse initBoulder = getEntities(initRes, "boulder").stream().findFirst().get();

        // get key
        DungeonResponse getKeyRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(getKeyRes, "key").size() == 1);

        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse openDoorRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(openDoorRes, "key").size() == 0);

        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        DungeonResponse boulderPushRes = dmc.tick(Direction.RIGHT);
        // test boulder moved into door
        assertEquals(new Position(3, 2), getEntityFromID(boulderPushRes, initBoulder.getId()).get().getPosition());
    }
}
