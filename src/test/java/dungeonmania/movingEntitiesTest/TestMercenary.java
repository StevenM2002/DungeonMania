package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMercenary {
    private static DungeonResponse genericMercenarySequence(DungeonManiaController controller, String configFile) {
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicMercenary", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
            String configFilePath) {
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
    public void MercenaryCantBribe() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryFriendly", "c_MercenaryCantBribe");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 2, y));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        res = dmc.tick(Direction.LEFT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        DungeonResponse postBattleResponse = genericMercenarySequence(dmc,
                "c_battleTests_MercenaryCantBribe");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, false, "c_MercenaryCantBribe");
    }

    @Test
    public void MercenaryFriendly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryFriendly", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 2, y));
        movementTrajectory.add(new Position(x + 2, y));
        movementTrajectory.add(new Position(x + 3, y));
        movementTrajectory.add(new Position(x + 3, y - 1));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        res = dmc.tick(Direction.LEFT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        for (int i = 0; i <= 1; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }

    @Test
    public void MercenaryMoveAroundWalls() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryWalls", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x + 2, y - 1));
        movementTrajectory.add(new Position(x + 3, y - 1));
        movementTrajectory.add(new Position(x + 4, y - 1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 5; ++i) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }

    @Test
    public void MercenaryMoveThroughDoors() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryDoors", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y));
        movementTrajectory.add(new Position(x, y));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 1; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }

    @Test
    public void MercenaryMoveThroughPortals() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryPortals", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 2, y + 4));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 1; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }
}
