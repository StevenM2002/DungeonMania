package dungeonmania.MovingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMercenary {

    @Test
    public void MercenaryCantBribe() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MercenaryFriendly", "c_Test_MercenaryCantBribe");
        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x + 1, y));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        try {
            res = dmc.interact(getEntities(res, "mercenary").get(0).getId());
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        res = dmc.tick(Direction.LEFT);

        assertTrue(dmc.getAllEntities().size() == 2);
        
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
        movementTrajectory.add(new Position(x + 3, y));
        movementTrajectory.add(new Position(x + 3, y - 1));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement++;

        try {
            res = dmc.interact(getEntities(res, "mercenary").get(0).getId());
        } catch (IllegalArgumentException | InvalidActionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x + 1, y + 1));
        movementTrajectory.add(new Position(x + 2, y + 1));
        movementTrajectory.add(new Position(x + 3, y + 1));
        movementTrajectory.add(new Position(x + 4, y + 1));

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.RIGHT);
            System.out.println(i);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "mercenary").get(0).getPosition());
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


        for (int i = 0; i <= 1; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement),
                    getEntities(res, "mercenary").get(0).getPosition());
            nextPositionElement++;
        }
    }
}
