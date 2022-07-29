package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getEntityFromID;
import java.util.List;

public class LogicSwitchesTests {
    @Test
    @DisplayName("Test logic switch activates and deactivates a lightbulb")
    public void testBasicOrLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicLightbulbTest", "c_shieldConstructionTest");
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        // pushed boulder on to switch
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        // test lightbulb deactivates when boulder is pushed off
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
    }

    @Test
    public void testOrWireAndLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_AndLightbulbOrWire", "c_shieldConstructionTest");
        // Push boulder on to switch, lightbulb should still stay off since its an AND
        DungeonResponse res = dmc.tick(Direction.LEFT);
        assertEquals(0, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        // Move the player to the other boulder to activate the other switch. This should turn the lightbulb on
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(0, getEntities(res, "light_bulb_off").size());
    }

    @Test
    public void testXORLightbulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_XORLightbulb", "c_shieldConstructionTest");
        // Push boulder on to switch, lightbulb should turn on
        DungeonResponse res = dmc.tick(Direction.LEFT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        // Move the player to the other boulder to activate the other switch. Should turn lightbulb off
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(0, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
    }

    @Test
    public void testCoAnd() {
        // Test co_and activation success
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_coandLightbulbTest", "c_shieldConstructionTest");
        assertEquals(2, getEntities(res, "light_bulb_off").size());
        // pushed boulder on to switch
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        
        // Test co_and activation failure with one adjacent active entity
        res = dmc.tick(Direction.UP);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        // Activate another entity next to the lightbulb. It should stay off since they weren't activated on the same tick.
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
    }

    @Test
    public void testLogicDoor() {
        
    }

    @Test
    public void testAndWires() {

    }
}
