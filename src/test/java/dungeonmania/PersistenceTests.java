package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersistenceTests {

    /**
     * Tests that all fields of the dungeon response are equal
     * @param prev
     * @param load
     */
    private void assertDungeonResponsesEqual(DungeonResponse prev, DungeonResponse load) {
        assertEquals(prev.getDungeonId(), load.getDungeonId());
        assertEquals(prev.getDungeonName(), load.getDungeonName());
        assertEquals(prev.getGoals(), load.getGoals());
        for (EntityResponse e : prev.getEntities()) {
            assertTrue(load.getEntities().stream().filter(x->x.equals(e)).count() == 1);
        }
        for (ItemResponse i : prev.getInventory()) {
            assertTrue(load.getInventory().stream().filter(x->x.equals(i)).count() == 1);
        }
        for (String b : prev.getBuildables()) {
            assertTrue(load.getBuildables().stream().filter(x->x.equals(b)).count() == 1);
        }
        for (int i = 0; i < prev.getBattles().size() && i < load.getBattles().size(); i++) {
            assertEquals(prev.getBattles().get(i), load.getBattles().get(i));
        }
    }


    @Test
    @DisplayName("Test advanced")
    public void testAdvancedDungeon() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initResponse = dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("SavedGame");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }


    @Test
    @DisplayName("Test moving before save")
    public void testSaveAfterMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        DungeonResponse initResponse = dmc.tick(Direction.RIGHT);
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("SavedGame");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }

    
    @Test
    @DisplayName("Test moving after loading")
    public void testMoveAfterLoad() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        dmc2.loadGame("SavedGame");
        assertDoesNotThrow(()->dmc2.tick(Direction.RIGHT));
        assertDoesNotThrow(()->dmc2.tick(Direction.RIGHT));
    }

    @Test
    @DisplayName("Test after using potion")
    public void testSaveAfterPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        DungeonResponse moveResponse = dmc.tick(Direction.RIGHT);
        String firstPotionID = moveResponse.getInventory().get(0).getId();
        String secondPotionID = moveResponse.getInventory().get(1).getId();
        assertDoesNotThrow(()->dmc.tick(firstPotionID));
        assertDoesNotThrow(()->dmc.tick(secondPotionID));
        DungeonResponse potionUseResponse = dmc.tick(Direction.UP);
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("SavedGame");
        assertDungeonResponsesEqual(potionUseResponse, loadResponse);
    }

    @Test
    @DisplayName("Test after using other potion")
    public void testSaveAfterOtherPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        DungeonResponse moveResponse = dmc.tick(Direction.RIGHT);
        String firstPotionID = moveResponse.getInventory().get(0).getId();
        String secondPotionID = moveResponse.getInventory().get(1).getId();
        assertDoesNotThrow(()->dmc.tick(secondPotionID));
        assertDoesNotThrow(()->dmc.tick(firstPotionID));
        DungeonResponse potionUseResponse = dmc.tick(Direction.UP);
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("SavedGame");
        assertDungeonResponsesEqual(potionUseResponse, loadResponse);
    }

    @Test
    @DisplayName("Test door unlocked")
    public void testDoorUnlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.DOWN);
        DungeonResponse initResponse = dmc.tick(Direction.DOWN);
        dmc.saveGame("SavedGame");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("SavedGame");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }


}
