package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InventoryEntitiesTest {
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), enemyAttack / 10);
            assertEquals(round.getDeltaEnemyHealth(), playerAttack / 5);
            enemyHealth -= round.getDeltaEnemyHealth();
            playerHealth -= round.getDeltaCharacterHealth();
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
        assertBattleCalculations("mercenary", battle, true, "c_bowTest");
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
        double playerAttackNoSword = Double.parseDouble(getValueFromConfigFile("player_attack", configFile));
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        // Pick up sword
        DungeonResponse res = controller.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        assertTrue(Double.parseDouble(getValueFromConfigFile("player_attack", configFile)) == playerAttackNoSword + 2);
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
        assertBattleCalculations("mercenary", battle, true, "c_swordTest");
        assertTrue(getInventory(postBattleResponse, "sword").size() == 0);
    }

    private static DungeonResponse shieldMercenarySequence(DungeonManiaController controller, String configFile) throws IllegalArgumentException, InvalidActionException {
        /*
         *  exit    wall   wall   wall   wall   wall  wall  wall
         * player   wood   wood treasure [  ]   [  ]  [  ]  merc  wall
         *  wall    wall   wall   wall   wall   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_shieldTreasureTest", configFile);
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
    @DisplayName("Test the shield construction works with treasure, and it works in combat")
    public void shieldConstructionTreasureTest() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_shieldConstructionTreasureTest", "c_shieldConstructionTest");

        // Battle
        DungeonResponse postBattleResponse = shieldMercenarySequence(dmc, "c_shieldConstructionTest");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true, "c_shieldConstructionTest");
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
        dmc.newGame("d_shieldConstructionKeyTest", "c_shieldConstructionTest");
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("shield");
        });
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("bow");
        });
    }
}
