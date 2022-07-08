package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMercenaryFriendly {
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
}
