package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

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
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicSwitchdoorTest", "c_shieldConstructionTest");
        // pushed boulder on to switch
        dmc.tick(Direction.LEFT);
        // Tries to move through the door, should work
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        Position expectedPlayerPosition = new Position(1, 1);
        DungeonResponse res = dmc.tick(Direction.DOWN);
        assertEquals(expectedPlayerPosition, getPlayer(res).get().getPosition());
        // Push boulder off the switch, should lock the door
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        expectedPlayerPosition = new Position(2, 1);
        assertEquals(expectedPlayerPosition, getPlayer(res).get().getPosition());
    }

    @Test
    public void testLogicBomb() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("Logicalbomb", "c_shieldConstructionTest");
        // Picking up bomb
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());
        // Place bomb
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        // Goes back to activate switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        // Next tick should blow stuff up
        assertEquals(10, dmc.getDungeonResponseModel().getEntities().size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(6, dmc.getDungeonResponseModel().getEntities().size());
    }

    @Test
    @DisplayName("Test that and wires stays on when its two adjacent activated entities are not the original two that activated it")
    public void testAndWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("AndWire2", "c_shieldConstructionTest");
        // Activate one adjacent entity
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        // Activates the other switch, should turn lightbulb on.
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        // Activates third switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        // Deactivate previous switch, lightbulb should stay on
        dmc.tick(Direction.DOWN);
        //dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "light_bulb_on").size());
    }

    @Test
    public void testThreeWayActivation() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ThreeWayConductionTest", "c_shieldConstructionTest");
        assertEquals(0, getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(3, getEntities(res, "light_bulb_on").size());
    }
}
