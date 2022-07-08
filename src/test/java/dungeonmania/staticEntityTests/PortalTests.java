package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getEntityFromID;

/**
 * testPortals:
 * [ ] [ ] [ ] [ ] [ ] -2
 * [ ] [o] [ ] [o] [w] -1
 * [ ] [ ] [ ] [ ] [ ]  0
 * [ ] [p] [o] [ ] [ ]  1
 * [ ] [ ] [ ] [w] [ ]  2
 * [ ] [o] [w] [o] [w]  3
 * [o] [w] [o] [w] [ ]  4
 * [ ] [ ] [o] [ ] [ ]  5
 *  0   1   2   3   4
 * 
 * testMercenaryPortal:
 * [ ] [ ] [o] [ ] [ ] -1
 * [ ] [ ] [w] [w] [ ]  0
 * [ ] [p] [o] [m] [w]  1
 * [ ] [w] [w] [w] [ ]  2
 *  0   1   2   3   4
 */
public class PortalTests {
    @Test
    @DisplayName("Test when the player teleports")
    public void playerTeleport() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testPortals", "c_staticTests_staticConfig");

        // move into portal
        dmc.tick(Direction.UP);
        DungeonResponse teleportRes = dmc.tick(Direction.UP);
        assertEquals(new Position(3, -2), getPlayer(teleportRes).get().getPosition());
    }

    @Test
    @DisplayName("Test when the player teleports twice")
    public void playerTeleportTwice() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testPortals", "c_staticTests_staticConfig");

        // move into portal
        dmc.tick(Direction.UP);
        DungeonResponse teleportRes = dmc.tick(Direction.UP);
        assertEquals(new Position(3, -2), getPlayer(teleportRes).get().getPosition());

        //move into second portal
        DungeonResponse secondTeleportRes = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 0), getPlayer(secondTeleportRes).get().getPosition());
    }


    @Test
    @DisplayName("Test when there is one wall adjacent to second portal")
    public void playerTeleportIntoWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testPortals", "c_staticTests_staticConfig");
        ArrayList<Position> posList = new ArrayList<Position>();
        posList.add(new Position(3, 0));
        posList.add(new Position(2, -1));
        posList.add(new Position(3, -2));

        // move into portal
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        DungeonResponse teleportRes = dmc.tick(Direction.RIGHT);
        assertTrue(posList.contains(getPlayer(teleportRes).get().getPosition()));
    }

    @Test
    @DisplayName("Test portal blocked by walls")
    public void testPortalBlockedByWalls() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testPortals", "c_staticTests_staticConfig");

        // teleport fails
        dmc.tick(Direction.DOWN);
        DungeonResponse teleportRes = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 2), getPlayer(teleportRes).get().getPosition());
    }

    @Test
    @DisplayName("Test when a portal leads to a second portal")
    public void testPortalChaining() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_testPortals", "c_staticTests_staticConfig");

        // also tests teleporting to single spot
        DungeonResponse teleportRes = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 5), getPlayer(teleportRes).get().getPosition());
        DungeonResponse secondTeleportRes = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 0), getPlayer(secondTeleportRes).get().getPosition());
    }

    @Test
    @DisplayName("Test if mercenary goes through portal")
    public void testMercenaryPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = dmc.newGame("d_staticTests_testMercenaryPortal", "c_staticTests_staticConfig");
        EntityResponse initMerc = getEntities(initRes, "mercenary").stream().findFirst().get();
        
        // player moves down, does nothing, merc should move into portal
        DungeonResponse secondRes = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, -1), getEntityFromID(secondRes, initMerc.getId()).get().getPosition());
    }
}
