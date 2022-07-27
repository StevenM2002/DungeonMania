package dungeonmania.MovingEntitiesTest;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntities.Mercenary;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMercenary {

    @Test
    public void MercenaryCantBribe() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_Test_MercenaryFriendly", "c_Test_MercenaryCantBribe");
        var res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 2), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        DungeonResponse finalRes = res;
        assertThrows(InvalidActionException.class, () -> dmc.interact(getEntities(finalRes, "mercenary").get(0).getId()));
        res = dmc.getDungeonResponseModel();
        assertEquals(new Position(2, 2), getEntities(res, "mercenary").get(0).getPosition());
        dmc.tick(Direction.LEFT);
        assertEquals(2, dmc.getAllEntities().size());
    }

    @Test
    public void MercenaryFriendly() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_Test_MercenaryFriendly", "c_battleTests_basicMercenaryMercenaryDies");
        var res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 2), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 2), getEntities(res, "mercenary").get(0).getPosition());
        DungeonResponse finalRes = res;
        assertDoesNotThrow(() -> dmc.interact(getEntities(finalRes, "mercenary").get(0).getId()));
        assertEquals(new Position(2, 2), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 2), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 1), getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    public void MercenaryMoveAroundWalls() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryWalls", "c_battleTests_basicMercenaryMercenaryDies");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 1), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getEntities(res, "mercenary").get(0).getPosition());
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


        for (int i = 0; i <= 1; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }
}
