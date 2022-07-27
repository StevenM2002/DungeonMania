package dungeonmania;

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
import java.util.List;

import dungeonmania.StaticEntities.LightBulb;
import dungeonmania.StaticEntities.LogicalSwitch;

public class LogicSwitchesTests {
    @Test
    @DisplayName("Test player move into door without key")
    public void testBasicLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicLightbulbTest", "c_shieldConstructionTest");
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        // pushed boulder on to switch
        res = dmc.tick(Direction.LEFT);
        System.out.println(((LightBulb) dmc.getAllEntities().get(0)).getActivated());
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        assertEquals(0, getEntities(res, "light_bulb_off").size());
    }
}
