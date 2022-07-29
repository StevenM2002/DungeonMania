package dungeonmania.MovingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.MidnightArmour;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sword;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class TestBattles {
    private List<InventoryObject> inventory;

    private void setInventory(List<InventoryObject> inventory) {
        this.inventory = inventory;
    }

    private List<InventoryObject> getLocalInventory() {
        return this.inventory;
    }

    private DungeonResponse genericSpiderSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicSpider", configFile);
        setInventory(controller.getPlayer().getInventory());
        int enemyCount = countEntityOfType(initialResponse, "spider");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.DOWN);
    }

    private DungeonResponse genericZombieSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicZombieToast", configFile);
        setInventory(controller.getPlayer().getInventory());
        int enemyCount = countEntityOfType(initialResponse, "zombie_toast");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.RIGHT);
    }

    private DungeonResponse genericHydraSequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicHydra", configFile);
        setInventory(controller.getPlayer().getInventory());
        int enemyCount = countEntityOfType(initialResponse, "hydra");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, enemyCount);
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(List<InventoryObject> inventory, String enemyType, BattleResponse battle, boolean enemyDies,
            String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double swordMod = 0;
        double shieldMod = 0;
        double bowMod = 1;
        double midnightAttackMod = 0;
        double midnightDefenceMod = 0;

        if (inventory.stream().anyMatch(e -> e instanceof Sword)) { 
            swordMod = Double.parseDouble(getValueFromConfigFile("sword_attack", configFilePath));;
        }

        if (inventory.stream().anyMatch(e -> e instanceof Shield)) {
            shieldMod = Double.parseDouble(getValueFromConfigFile("shield_defence", configFilePath));
        }

        if (inventory.stream().anyMatch(e -> e instanceof Bow)) {
            bowMod = 2;
        }

        if (inventory.stream().anyMatch(e -> e instanceof MidnightArmour)) {
            midnightAttackMod = Double.parseDouble(getValueFromConfigFile("midnight_armour_attack", configFilePath));
            midnightDefenceMod = Double.parseDouble(getValueFromConfigFile("midnight_armour_defence", configFilePath));
        }

        for (RoundResponse round : rounds) {
            assertEquals(-((enemyAttack - shieldMod - midnightDefenceMod) / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-((bowMod * (playerAttack + swordMod + midnightAttackMod)) / 5), round.getDeltaEnemyHealth(), 0.001);
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
        assertBattleCalculations(getLocalInventory(), "spider", battle, false, "c_battleTests_basicSpiderPlayerDies");
    }

    @Test
    public void testRoundCalculationsSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericSpiderSequence(controller,
                "c_battleTests_basicSpiderDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(getLocalInventory(), "spider", battle, true, "c_battleTests_basicSpiderDies");
    }

    @Test
    public void testHealthBelowZeroZombieToast() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericZombieSequence(controller,
                "c_battleTests_basicZombiePlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(getLocalInventory(), "zombie", battle, false, "c_battleTests_basicZombiePlayerDies");
    }

    @Test
    public void testRoundCalculationsZombieToast() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericZombieSequence(controller,
                "c_battleTests_basicZombieDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(inventory, "zombie", battle, true, "c_battleTests_basicZombieDies");
    }

    @Test
    public void testRoundCalculationsHydra() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraHydraDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(getLocalInventory(), "hydra", battle, true, "c_battleTests_basicHydraHydraDies");
    }

    @Test
    public void testHealthBelowZeroHydra() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraPlayerDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(getLocalInventory(), "hydra", battle, true, "c_battleTests_basicHydraPlayerDies");
    }

    @Test
    public void testHydraHeal() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericHydraSequence(controller,
                "c_battleTests_basicHydraHeal");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertHydraBattleCalculations("hydra", battle, false, "c_battleTests_basicHydraHeal");
    }


    private DungeonResponse MercenarySequenceSword(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_MercenarySword", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    @Test 
    public void testWeaponSword() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = MercenarySequenceSword(controller,"c_battleTests_basicMercenarySword");
        setInventory(controller.getPlayer().getInventory());
        assertEquals(true, controller.getPlayer().getInventory().stream().anyMatch(e -> e instanceof Sword));
        postBattleResponse = controller.tick(Direction.LEFT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations(controller.getPlayer().getInventory(), "mercenary", battle, true, "c_battleTests_basicMercenarySword");
    }
}
