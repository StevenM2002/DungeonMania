package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;

public class WallTests {

    @Test
    @DisplayName("Test wall blocks player movement")
    public void testWallBlock() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testWallBlocking", "c_staticTests_staticConfig");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        EntityResponse expectedinitialPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);
        EntityResponse expectedPlayer2 = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 0), false);

        // wall at (1, 2) and (2, 1)
        DungeonResponse actualDungeonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedinitialPlayer, actualPlayer);
        actualDungeonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedinitialPlayer, actualPlayer);

        dmc.tick(Direction.UP);
        actualDungeonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungeonRes).get();
        assertEquals(expectedPlayer2, actualPlayer);
    }
}
