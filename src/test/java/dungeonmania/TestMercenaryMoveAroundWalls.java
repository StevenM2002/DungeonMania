package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMercenaryMoveAroundWalls {
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
}
