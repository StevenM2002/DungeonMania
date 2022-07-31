package dungeonmania.TimeTravelTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import static dungeonmania.TestUtils.getPlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeTurnerTests {

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
    @DisplayName("Test player successfully picks up time turner")
    public void testPickupTimeTurner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_timeTravelTests_timeTurner", "c_timeTravelTests");
        assertEquals(1, initRes.getEntities().stream().filter(x->x.getType().equals("time_turner")).count());
        DungeonResponse afterMove = dmc.tick(Direction.RIGHT);
        assertEquals(0, afterMove.getEntities().stream().filter(x->x.getType().equals("time_turner")).count());
    }

    @Test
    @DisplayName("Test player rewinds one tick")
    public void testTimeTravelOneTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_timeTravelTests_timeTurner", "c_timeTravelTests");
        DungeonResponse toTravelRes = dmc.tick(Direction.RIGHT);
        DungeonResponse beforeTravelRes = dmc.tick(Direction.RIGHT);
        DungeonResponse afterTravelRes = dmc.rewind(1);
        assertTimeTravelEntitiesEqual(toTravelRes, afterTravelRes);
        assertTimeTravelInventoryEqual(beforeTravelRes, afterTravelRes);
        assertPlayerAndOldPlayerMatchUp(toTravelRes, afterTravelRes);
    }

    @Test
    @DisplayName("Test player rewinds 5 ticks")
    public void testFiveTicks() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_timeTravelTests_timeTurner", "c_timeTravelTests");
        DungeonResponse toTravelRes = dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse beforeTravelRes = dmc.tick(Direction.RIGHT);
        DungeonResponse afterTravelRes = dmc.rewind(5);
        assertTimeTravelEntitiesEqual(toTravelRes, afterTravelRes);
        assertTimeTravelInventoryEqual(beforeTravelRes, afterTravelRes);
        assertPlayerAndOldPlayerMatchUp(toTravelRes, afterTravelRes);
    }

    @Test
    @DisplayName("Test player tries to rewind more ticks than have happened")
    public void testRewindToStart() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse toTravelRes = dmc.newGame("d_timeTravelTests_timeTurner", "c_timeTravelTests");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        DungeonResponse beforeTravelRes = dmc.tick(Direction.RIGHT);
        DungeonResponse afterTravelRes = dmc.rewind(5);
        assertTimeTravelEntitiesEqual(toTravelRes, afterTravelRes);
        assertTimeTravelInventoryEqual(beforeTravelRes, afterTravelRes);
        assertPlayerAndOldPlayerMatchUp(toTravelRes, afterTravelRes);
    }

    @Test
    @DisplayName("Test older player moves correctly")
    public void testOlderPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse tick0 = dmc.newGame("d_timeTravelTests_timeTurner", "c_timeTravelTests");
        DungeonResponse tick1 = dmc.tick(Direction.RIGHT);
        DungeonResponse tick2 = dmc.tick(Direction.DOWN);
        DungeonResponse tick3 = dmc.tick(Direction.RIGHT);
        DungeonResponse tick4 = dmc.tick(Direction.DOWN);
        DungeonResponse tick5 = dmc.tick(Direction.RIGHT);
        DungeonResponse afterTravelRes = dmc.rewind(5); // tick 0
        DungeonResponse afterTravel1 = dmc.tick(Direction.UP);
        DungeonResponse afterTravel2 = dmc.tick(Direction.LEFT);
        DungeonResponse afterTravel3 = dmc.tick(Direction.LEFT);
        DungeonResponse afterTravel4 = dmc.tick(Direction.UP);
        DungeonResponse afterTravel5 = dmc.tick(Direction.LEFT);
        assertPlayerAndOldPlayerMatchUp(tick0, afterTravelRes);
        assertPlayerAndOldPlayerMatchUp(tick1, afterTravel1);
        assertPlayerAndOldPlayerMatchUp(tick2, afterTravel2);
        assertPlayerAndOldPlayerMatchUp(tick3, afterTravel3);
        assertPlayerAndOldPlayerMatchUp(tick4, afterTravel4);
        assertPlayerAndOldPlayerMatchUp(tick5, afterTravel5);
    }

    @Test
    @DisplayName("Test battling older player")
    public void testOlderPlayerBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_timeTravelTests_testOlderPlayer", "c_timeTravelTests");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.rewind(3);
        DungeonResponse battleRes = dmc.tick(Direction.LEFT);
        assertTrue(battleRes.getBattles().stream().anyMatch(x->x.getEnemy().equals("older_player")));
    }

    @Test
    @DisplayName("Test no battle with sunstone")
    public void testNoBattleSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_timeTravelTests_testOlderPlayer", "c_timeTravelTests");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.rewind(5);
        DungeonResponse battleRes = dmc.tick(Direction.UP);
        assertFalse(battleRes.getBattles().stream().anyMatch(x->x.getEnemy().equals("older_player")));
    }
}
