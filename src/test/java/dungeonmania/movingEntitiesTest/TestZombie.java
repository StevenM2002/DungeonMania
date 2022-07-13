package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestZombie {
    @Test
    public void ZombieToastBlockable() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_ZombieToastBloackable", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "zombie_toast").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x, y));

        for (int i = 0; i < 1; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "zombie_toast").get(0).getPosition());
            nextPositionElement++;
        }
    }

    @Test
    public void ZombieToastSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_ZombieToastSwitch", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "zombie_toast").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x, y));

        for (int i = 0; i < 1; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "zombie_toast").get(0).getPosition());
            nextPositionElement++;
        }
    }
}
