package dungeonmania.MovingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestMobsRun {
    @Test
    public void MobsRun() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_Test_MobsRun", "c_battleTests_basicMercenaryMercenaryDies");
        Position posZombie = getEntities(res, "zombie_toast").get(0).getPosition();
        Position posMercenary = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        List<Position> movementTrajectory2 = new ArrayList<Position>();
        int x2 = posMercenary.getX();
        int y2 = posMercenary.getY();
        int nextPositionElement1 = 0;
        int nextPositionElement2 = 0;
        movementTrajectory.add(new Position(x2 + 1, y2));
        movementTrajectory.add(new Position(x2, y2));
        movementTrajectory.add(new Position(x2 - 1, y2));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(movementTrajectory.get(nextPositionElement1), getEntities(res, "mercenary").get(0).getPosition());
        nextPositionElement1++;

        int x1 = posZombie.getX();
        int y1 = posZombie.getY();
        movementTrajectory2.add(new Position(x1 + 1, y1));

        try {
            res = dmc.tick(getInventory(res, "invincibility_potion").get(0).getId());
            assertEquals(movementTrajectory.get(nextPositionElement1),
                    getEntities(res, "mercenary").get(0).getPosition());
            assertEquals(movementTrajectory.get(nextPositionElement2),
                    getEntities(res, "zombie_toast").get(0).getPosition());
        } catch (IllegalArgumentException | InvalidActionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
