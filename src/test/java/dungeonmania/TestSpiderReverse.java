package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestSpiderReverse {
    @Test
    @DisplayName("Test to see if a spider reverses direction object")
    public void SpiderReverse() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse res = game.newGame("d_Test_SpiderReverse", "c_Test_SpiderConfig");
        Position pos = getEntities(res, "spider").get(0).getPosition();
        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y - 1));
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x - 1, y - 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y + 1));
        movementTrajectory.add(new Position(x + 1, y));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 10; ++i) {
            res = game.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());

            nextPositionElement++;
        }

    }
}
