package dungeonmania.MovingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class TestBattles {
    private DungeonResponse genericSpiderSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicSpider", configFile);
        int enemyCount = countEntityOfType(initialResponse, "spider");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.DOWN);
    }

    private DungeonResponse genericZombieSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicZombieToast", configFile);
        int enemyCount = countEntityOfType(initialResponse, "zombie_toast");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.RIGHT);
    }

    private DungeonResponse genericHydraSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicHydra", configFile);
        int enemyCount = countEntityOfType(initialResponse, "hydra");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack/ 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    private void assertHydraBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
            String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(Double.parseDouble(getValueFromConfigFile("hydra_health_increase_amount", configFilePath)), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    @Test
    public void testHealthBelowZeroSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericSpiderSequence(controller,
                "c_battleTests_basicSpiderPlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("spider", battle, false, "c_battleTests_basicSpiderPlayerDies");
    }

    @Test
    public void testRoundCalculationsSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericSpiderSequence(controller,
                "c_battleTests_basicSpiderDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("spider", battle, true, "c_battleTests_basicSpiderDies");
    }

    @Test
    public void testHealthBelowZeroZombieToast() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericZombieSequence(controller,
                "c_battleTests_basicZombiePlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("zombie", battle, false, "c_battleTests_basicZombiePlayerDies");
    }

    @Test
    public void testRoundCalculationsZombieToast() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericZombieSequence(controller,
                "c_battleTests_basicZombieDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("zombie", battle, true, "c_battleTests_basicZombieDies");
    }

    @Test
    public void testRoundCalculationsHydra() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraHydraDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("hydra", battle, true, "c_battleTests_basicHydraHydraDies");
    }

    @Test
    public void testHealthBelowZeroHydra() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraPlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("hydra", battle, true, "c_battleTests_basicHydraPlayerDies");
    }

    @Test
    public void testHydraHeal() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraHeal");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertHydraBattleCalculations("hydra", battle, false, "c_battleTests_basicHydraHeal");
    }
}
