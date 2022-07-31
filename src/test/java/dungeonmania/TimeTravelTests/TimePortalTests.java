package dungeonmania.TimeTravelTests;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static dungeonmania.TestUtils.getPlayer;


public class TimePortalTests {

    /**
     * Tests that all entities (except the player) exist and have the same properties
     * across the dungeonResponses prev and load.
     * @param prev - the original dungeonresponse at tick
     * @param travel - the dungeonResponse after travelling back to tick
     */
    private void assertTimeTravelEntitiesEqual(DungeonResponse prev, DungeonResponse travel) {
        for (EntityResponse e : prev.getEntities()) {
            if (!e.getType().equals("player")){           
                if (travel.getEntities().stream().filter(x->x.equals(e)).count() != 1) {
                    System.out.println("actual entity: "+e.toString());
                    System.out.println("new entities: ");
                    travel.getEntities().stream().filter(x->x.getType().equals(e.getType())).forEach(x->System.out.println(x.toString()));
                }
                assertEquals(1, travel.getEntities().stream().filter(x->x.equals(e)).count());
            }
        }
    }

    /**
     * Asserts that the inventory of the player remains the same after time travelling
     * @param before - response of tick directly before time travelling
     * @param after - response of tick after time travelling
     */
    private void assertTimeTravelInventoryEqual(DungeonResponse before, DungeonResponse after) {
        for (ItemResponse i : before.getInventory()) {
            assertTrue(after.getInventory().stream().anyMatch(x->x.equals(i)));
        }
    }

    private void assertPlayerAndOldPlayerMatchUp(DungeonResponse oldDungeon, DungeonResponse afterRewind) {
        EntityResponse olderPlayer = afterRewind.getEntities().stream().filter(x->x.getType().equals("older_player")).findAny().orElse(null);
        EntityResponse actualOlderPlayer = getPlayer(oldDungeon).orElse(null);
        assertNotEquals(null, actualOlderPlayer);
        assertNotEquals(null, olderPlayer);
        assertEquals(actualOlderPlayer.getPosition(), olderPlayer.getPosition());
    }

    @Test
    @DisplayName("Test travelling back exactly 30 ticks with the portal")
    public void testTravel30Ticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_timeTravelTests_timePortal", "c_timeTravelTests");
        DungeonResponse tick1 = dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        DungeonResponse beforeTravel = dmc.tick(Direction.UP);
        DungeonResponse timeTravelRes = dmc.tick(Direction.RIGHT);
        assertTimeTravelEntitiesEqual(tick1, timeTravelRes);
        assertTimeTravelInventoryEqual(beforeTravel, timeTravelRes);
        assertPlayerAndOldPlayerMatchUp(tick1, timeTravelRes);
    }

    @Test
    @DisplayName("Test player ends up in same square as portal")
    public void testMoveIntoPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_timeTravelTests_timePortal", "c_timeTravelTests");
        EntityResponse portal = initRes.getEntities().stream().filter(x->x.getType().equals("time_travelling_portal")).findAny().get();
        DungeonResponse finalRes = dmc.tick(Direction.RIGHT);
        assertEquals(portal.getPosition(), getPlayer(finalRes).get().getPosition());
    }

    @Test
    @DisplayName("Test travel to before start of game")
    public void testBeforeTick0Travel() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_timeTravelTests_timePortal", "c_timeTravelTests");
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        DungeonResponse beforeTravel = dmc.tick(Direction.UP);
        DungeonResponse travelRes = dmc.tick(Direction.RIGHT);
        assertTimeTravelEntitiesEqual(initRes, travelRes);
        assertTimeTravelInventoryEqual(beforeTravel, travelRes);
        assertPlayerAndOldPlayerMatchUp(initRes, travelRes);
    }

}
