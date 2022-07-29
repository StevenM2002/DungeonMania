package dungeonmania.movementTests;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowMovementTest {
    @Test
    public void simpleFollowMovementRightDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_test_follow_movement_down_right_quad", "c_battleTests_basicMercenaryMercenaryDies");
        assertEquals (new Position(3, -1), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals (new Position(3, 0), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals (new Position(2, 0), getEntities(res, "mercenary").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals (new Position(1, 0), getEntities(res, "mercenary").get(0).getPosition());
    }
    @Test
    public void simpleFollowMovementRightUp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_test_follow_movement_up_right_quad", "c_battleTests_basicMercenaryMercenaryDies");
        List<Position> movementTrajectory = new ArrayList<>();
        movementTrajectory.add(new Position(3, 1));
        movementTrajectory.add(new Position(3, 0));
        movementTrajectory.add(new Position(2, 0));
        movementTrajectory.add(new Position(1, 0));

        for (int i = 0; i < movementTrajectory.size(); i++) {
            assertEquals (movementTrajectory.get(i), getEntities(res, "mercenary").get(0).getPosition());
            res = dmc.tick(Direction.UP);
        }
    }
    @Test
    public void simpleFollowMovementLeftDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_test_follow_movement_down_left", "c_battleTests_basicMercenaryMercenaryDies");
        List<Position> movementTrajectory = new ArrayList<>();
        movementTrajectory.add(new Position(-1, -1));
        movementTrajectory.add(new Position(-1, 0));
        movementTrajectory.add(new Position(0, 0));
        movementTrajectory.add(new Position(1, 0));

        for (int i = 0; i < movementTrajectory.size(); i++) {
            assertEquals (movementTrajectory.get(i), getEntities(res, "mercenary").get(0).getPosition());
            res = dmc.tick(Direction.UP);
        }
    }
    @Test
    public void simpleFollowMovementUpLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_test_follow_movement_up_left", "c_battleTests_basicMercenaryMercenaryDies");
        List<Position> movementTrajectory = new ArrayList<>();
        movementTrajectory.add(new Position(-1, 1));
        movementTrajectory.add(new Position(-1, 0));
        movementTrajectory.add(new Position(0, 0));
        movementTrajectory.add(new Position(1, 0));

        for (int i = 0; i < movementTrajectory.size(); i++) {
            assertEquals (movementTrajectory.get(i), getEntities(res, "mercenary").get(0).getPosition());
            res = dmc.tick(Direction.UP);
        }
    }

    @Test
    public void dikjstraDoesNotGoThroughSwamp() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_dijkstraMovement3_m3", "c_m3_config");
        assertEquals(new Position(1,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(1,2), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(2,2), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,2), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(4,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
    }

    @Test
    public void dikjstraGoesThroughSwamp() {
        var dmc = new DungeonManiaController();
        dmc.newGame("d_dikjstraMovement2_m3", "c_m3_config");
        assertEquals(new Position(1,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(2,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(2,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(2,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(3,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
        assertEquals(new Position(4,1), getEntities(dmc.tick(Direction.UP), "assassin").get(0).getPosition());
    }
}
