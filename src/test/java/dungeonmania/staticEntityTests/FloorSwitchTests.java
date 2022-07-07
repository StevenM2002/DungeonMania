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

import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntityFromID;

/** 
 * MAP:
 * [ ] [b] [s] 0
 * [p] [ ] [ ] 1
 * [ ] [b] [s] 2
 *  1   2   3
 */ 
public class FloorSwitchTests {
    @Test
    @DisplayName("Test player moving on and off switches")
    public void testPlayerOnSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testFloorSwitch", "c_staticTests_staticConfig");
        EntityResponse initPlayer = getPlayer(res).get();
        assertTrue(getGoals(res).contains(":boulders"));
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // standing on first switch
        DungeonResponse res1 = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res1).contains(":boulders"));
        assertEquals(new Position(3, 2), getEntityFromID(res1, initPlayer.getId()).get().getPosition());

        // moving off first switch and standing on second
        dmc.tick(Direction.UP);
        DungeonResponse res2 = dmc.tick(Direction.UP);
        assertTrue(getGoals(res2).contains(":boulders"));
        assertEquals(new Position(3, 0), getEntityFromID(res2, initPlayer.getId()).get().getPosition());
    }

    @Test
    @DisplayName("Test putting boulders on switches")
    public void testBoulderOnSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testFloorSwitch", "c_staticTests_staticConfig");
        EntityResponse initTopBoulder = res.getEntities().stream().filter(x->(x.getPosition() == new Position(2, 0) && x.getType() == "boulder")).findFirst().get();
        assertTrue(getGoals(res).contains(":boulders"));
        
        dmc.tick(Direction.UP);
        DungeonResponse res1 = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res1).contains(":boulders"));
        assertEquals(new Position(3, 0), getEntityFromID(res1, initTopBoulder.getId()).get().getPosition());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse res2 = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res2));
    }

    @Test
    @DisplayName("Test moving boulder off switch")
    public void testBoulderOffSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testFloorSwitch", "c_staticTests_staticConfig");
        EntityResponse initTopBoulder = res.getEntities().stream().filter(x->(x.getPosition() == new Position(2, 0) && x.getType() == "boulder")).findFirst().get();
        assertTrue(getGoals(res).contains(":boulders"));
        
        // push boulder onto and then off switch
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        DungeonResponse res1 = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res1).contains(":boulders"));
        assertEquals(new Position(4, 0), getEntityFromID(res1, initTopBoulder.getId()).get().getPosition());

        // push second boulder onto switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse res2 = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res2).contains(":boulders"));
    }
}
