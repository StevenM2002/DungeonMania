package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
            if (load.getEntities().stream().filter(x->x.equals(e)).count() != 1) {
                System.out.println("actual entity: "+e.toString());
                System.out.println("loaded entities: ");
                load.getEntities().stream().filter(x->x.getType().equals(e.getType())).forEach(x->System.out.println(x.toString()));
            }
            assertEquals(1, load.getEntities().stream().filter(x->x.equals(e)).count());
        }
        for (ItemResponse i : prev.getInventory()) {
            assertEquals(1, load.getInventory().stream().filter(x->x.equals(i)).count());
        }
        for (String b : prev.getBuildables()) {
            assertEquals(1, load.getBuildables().stream().filter(x->x.equals(b)).count());
        }
        for (int i = 0; i < prev.getBattles().size() && i < load.getBattles().size(); i++) {
            assertEquals(prev.getBattles().get(i), load.getBattles().get(i));
        }
    }


    @Test
    @DisplayName("Test advanced")
    public void testAdvancedDungeon() {
        System.out.println("advanced test");
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initResponse = dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.saveGame("AdvancedSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("AdvancedSave");
        assertDungeonResponsesEqual(initResponse, loadResponse);
        System.out.println("end advanced test");
    }

    @Test
    @DisplayName("Test moving before save")
    public void testSaveAfterMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        DungeonResponse initResponse = dmc.tick(Direction.RIGHT);
        dmc.saveGame("MovingSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("MovingSave");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }

    @Test
    @DisplayName("Test moving after loading")
    public void testMoveAfterLoad() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.saveGame("MoveAfterSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        dmc2.loadGame("MoveAfterSave");
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
        dmc.saveGame("PotionSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("PotionSave");
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
        dmc.saveGame("Potion2Save");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("Potion2Save");
        assertDungeonResponsesEqual(potionUseResponse, loadResponse);
    }

    @Test
    @DisplayName("Test door unlocked")
    public void testDoorUnlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_persistenceTests_advanced", "c_persistenceTests");
        dmc.tick(Direction.DOWN);
        DungeonResponse initResponse = dmc.tick(Direction.DOWN);
        dmc.saveGame("DoorSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("DoorSave");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }

    @Test
    @DisplayName("Test after battling a zombie toast")
    public void testAfterBattles() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_basicZombieToast", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        DungeonResponse initResponse = dmc.saveGame("BattleSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("BattleSave");
        assertDungeonResponsesEqual(initResponse, loadResponse);
    }

    @Test
    @DisplayName("Test after bribing the mercenary")
    public void testAfterBribe() {
        // sleep to make sure prev
        System.out.println("testing bribe");
        try {Thread.sleep(1000);} catch (InterruptedException e){System.err.println("sleep failed");}
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initResponse = dmc.newGame("d_percistenceTests_mercenaryBribe", "c_persistenceTests");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse finalTickRes = dmc.getDungeonResponseModel();
        EntityResponse mercenary = finalTickRes.getEntities().stream().filter(x->x.getType().equals("mercenary")).findFirst().get();
        EntityResponse player = finalTickRes.getEntities().stream().filter(x->x.getType().equals("player")).findFirst().get();
        System.out.println("player pos"+player.getPosition().toString());
        System.out.println("merc pos: "+mercenary.getPosition().toString());
        assertDoesNotThrow(()->dmc.interact(mercenary.getId()));
        DungeonResponse saveResponse = dmc.saveGame("BribeSave");
        DungeonManiaController dmc2 = new DungeonManiaController();
        DungeonResponse loadResponse = dmc2.loadGame("BribeSave");
        assertDungeonResponsesEqual(saveResponse, loadResponse);
    }


    public static void main(String[] args) {
        PersistenceTests p = new PersistenceTests();
        p.testAdvancedDungeon();
        p.testSaveAfterMovement();
        p.testMoveAfterLoad();
    }
}
