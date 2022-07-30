package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntities.Assassin;
import dungeonmania.MovingEntities.FriendlyMovement;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.RunningMovement;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class InventoryEntitiesTest {
    private void assertBattleCalculationsOneAccessory(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath, String accessory) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double swordBuff = Double.parseDouble(getValueFromConfigFile("sword_attack", configFilePath));
        double shieldDefence = Double.parseDouble(getValueFromConfigFile("shield_defence", configFilePath));
        double midnightArmorBuff = Double.parseDouble(getValueFromConfigFile("midnight_armour_attack", configFilePath));
        double midnightArmorDefence = Double.parseDouble(getValueFromConfigFile("midnight_armour_defence", configFilePath));
        if (accessory.equals("sword")) {
            for (RoundResponse round : rounds) {
                assertEquals(round.getDeltaCharacterHealth(), -enemyAttack / 10);
                assertEquals(round.getDeltaEnemyHealth(), -(playerAttack + swordBuff) / 5);
                enemyHealth += round.getDeltaEnemyHealth();
                playerHealth += round.getDeltaCharacterHealth();
            }
        }
        if (accessory.equals("bow")) {
            for (RoundResponse round : rounds) {
                assertEquals(round.getDeltaCharacterHealth(), enemyAttack / 10);
                assertEquals(round.getDeltaEnemyHealth(), (playerAttack * 2) / 5);
                enemyHealth -= round.getDeltaEnemyHealth();
                playerHealth -= round.getDeltaCharacterHealth();
            }
        }
        if (accessory.equals("shield")) {
            for (RoundResponse round : rounds) {
                assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack - shieldDefence) / 10);
                assertEquals(round.getDeltaEnemyHealth(), -playerAttack / 5);
                enemyHealth += round.getDeltaEnemyHealth();
                playerHealth += round.getDeltaCharacterHealth();
            }
        }
        if (accessory.equals("midnight_armour")) {
            System.out.println(playerAttack + midnightArmorBuff);
            for (RoundResponse round : rounds) {
                assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack - midnightArmorDefence) / 10);
                assertEquals(round.getDeltaEnemyHealth(), -(playerAttack + midnightArmorBuff)/ 5);
                enemyHealth += round.getDeltaEnemyHealth();
                playerHealth += round.getDeltaCharacterHealth();
            }
        }
        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }
    
    @Test
    @DisplayName("Test player can contruct bows and fight with it")
    public void bowConstruction() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bowTest", "c_bowTest");
        assertTrue(getInventory(res, "wood").size() == 0);
        assertTrue(getInventory(res, "arrow").size() == 0);
        assertTrue(getInventory(res, "bow").size() == 0);

        // Picks up wood and 3 arrows
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());

        // Build a bow
        res = dmc.build("bow");
        assertTrue(getInventory(res, "bow").size() == 1);
        assertTrue(getInventory(res, "wood").size() == 0);
        assertTrue(getInventory(res, "arrow").size() == 0);

        // Encounters mercenary
        DungeonResponse postBattleResponse = dmc.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculationsOneAccessory("mercenary", battle, true, "c_bowTest", "bow");
        // Test bow disappeared
        assertTrue(getInventory(postBattleResponse, "bow").size() == 0);
    }

    private static DungeonResponse swordMercenarySequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall  wall
         * player  sword [  ]  [  ]  merc  wall
         *  wall   wall  wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_swordTest", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        assertTrue(getInventory(initialResponse, "sword").size() == 0);
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        // Pick up sword
        DungeonResponse res = controller.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        // Encounters mercenary
        return controller.tick(Direction.RIGHT);
    }

    @Test
    @DisplayName("Test the sword will help the player win when he otherwise wouldn't")
    public void swordAttack() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_basicMercenary", "c_swordTest");

        // Battle
        DungeonResponse postBattleResponse = swordMercenarySequence(dmc, "c_swordTest");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculationsOneAccessory("mercenary", battle, true, "c_swordTest", "sword");
        assertTrue(getInventory(postBattleResponse, "sword").size() == 0);
    }

    private static DungeonResponse shieldMercenarySequence(DungeonManiaController controller, String configFile) throws IllegalArgumentException, InvalidActionException {
        /*
         *  exit    wall   wall   wall   wall   wall  wall  wall
         * player   wood   wood treasure [  ]   [  ]  [  ]  merc  wall
         *  wall    wall   wall   wall   wall   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_shieldConstructionTreasureTest", configFile);
        assertTrue(getInventory(initialResponse, "shield").size() == 0);

        // Pick up construction material and builds a shield
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        DungeonResponse res = controller.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());
        res = controller.build("shield");
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());

        // Encounters mercenary
        return controller.tick(Direction.RIGHT);
    }

    @Test
    @DisplayName("Test the shield construction works with treasure")
    public void shieldConstructionTreasureTest() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_shieldConstructionTreasureTest", "c_shieldConstructionTest");

        // Battle
        DungeonResponse postBattleResponse = shieldMercenarySequence(dmc, "c_shieldConstructionTest");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculationsOneAccessory("mercenary", battle, true, "c_shieldConstructionTest", "shield");
        assertEquals(0, getInventory(postBattleResponse, "shield").size());
    }

    @Test
    @DisplayName("Test shield construction works with keys")
    public void shieldConstructionKeyTest() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldConstructionKeyTest", "c_shieldConstructionTest");
        assertTrue(getInventory(res, "shield").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());
        res = dmc.build("shield");
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "key").size());
    }

    @Test
    @DisplayName("Test build with inadequate materials")
    public void constructionInadequateMaterials() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_shieldConstructionTreasureTest", "c_shieldConstructionTest");
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("shield");
        });
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("bow");
        });
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("sceptre");
        });
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("midnight_armour");
        });
    }

    @Test
    @DisplayName("Test midnight armor successfully builds and helps in battle")
    public void constructMidnightArmor() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmorTest", "c_midnightArmorTest");
        assertTrue(getInventory(res, "midnight_armour").size() == 0);

        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "sword").size());
        res = dmc.build("midnight_armour");
        assertEquals(0, getInventory(res, "sun_stone").size());
        assertEquals(0, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "midnight_armour").size());

        // Encounters Mercenary
        dmc.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = dmc.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculationsOneAccessory("mercenary", battle, true, "c_midnightArmorTest", "midnight_armour");
    }

    @Test
    @DisplayName("Test midnight armor fails to build when there are zombies")
    public void armorConstructionZombies() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_midnightArmorTestWithZombies", "c_midnightArmorTest");
        dmc.tick(Direction.RIGHT);
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("midnight_armour");
        });
    }

    @Test
    @DisplayName("Test sceptre build with wood and key")
    public void sceptreBuildWoodAndKey() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreBuildTestWoodAndKey", "c_midnightArmorTest");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "wood").size());
        res = dmc.build("sceptre");
        assertEquals(0, getInventory(res, "sun_stone").size());
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sceptre").size());
    }

    @Test
    @DisplayName("Test sceptre build with arrows and treasure")
    public void sceptreBuildArrowsAndTreasure() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreBuildTestArrowsAndTreasure", "c_midnightArmorTest");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(2, getInventory(res, "arrow").size());
        res = dmc.build("sceptre");
        assertEquals(0, getInventory(res, "sunstone").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "sceptre").size());
    }

    @Test
    @DisplayName("Test sceptre build with wood and 2 sunstones")
    public void sceptreBuildTwoSunstones() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreBuildTestTwoSunstones", "c_midnightArmorTest");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "wood").size());
        res = dmc.build("sceptre");
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sceptre").size());
    }

    @Test
    @DisplayName("Test shield construction with sunstone")
    public void shieldBuildTwoSunstones() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_shieldConstructionSunstoneTest", "c_shieldConstructionTest");
        assertTrue(getInventory(res, "shield").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        res = dmc.build("shield");
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test sceptre mind controlling mercenary.")
    public void testMindControlMercenary() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mindControlMercenaryTest", "c_mindControl");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        assertEquals(1, getInventory(res, "sceptre").size());
        Mercenary merc = (Mercenary) dmc.getAllEntities().stream().filter(e -> e instanceof Mercenary).findFirst().get();
        res = dmc.interact(merc.getId());
        assertTrue(getInventory(res, "sceptre").size() == 0);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(merc.getMovementStrategy() instanceof FriendlyMovement);
    }

    @Test
    @DisplayName("Test sceptre mind controlling assassin.")
    public void testMindControlAssassin() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mindControlAssassinTest", "c_mindControl");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        assertEquals(1, getInventory(res, "sceptre").size());
        Assassin merc = (Assassin) dmc.getAllEntities().stream().filter(e -> e instanceof Assassin).findFirst().get();
        res = dmc.interact(merc.getId());
        assertTrue(getInventory(res, "sceptre").size() == 0);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(merc.getMovementStrategy() instanceof FriendlyMovement);
    }

    @Test
    @DisplayName("Test sceptre mind control during invincibility.")
    public void testMindControlThenInvincibility() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mindControlInvincibilityTest", "c_mindControl");
        assertTrue(getInventory(res, "sceptre").size() == 0);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.build("sceptre");
        assertEquals(1, getInventory(res, "sceptre").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        dmc.tick(getInventory(res, "invincibility_potion").get(0).getId());
        Mercenary merc = (Mercenary) dmc.getAllEntities().stream().filter(e -> e instanceof Mercenary).findFirst().get();
        assertTrue(merc.getMovementStrategy() instanceof RunningMovement);
        res = dmc.interact(merc.getId());
        assertTrue(getInventory(res, "sceptre").size() == 0);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(merc.getMovementStrategy() instanceof FriendlyMovement);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(merc.getMovementStrategy() instanceof FriendlyMovement);
    }
}
