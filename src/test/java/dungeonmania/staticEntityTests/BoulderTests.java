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
import static dungeonmania.TestUtils.getEntityFromID;

/**
 * MAP:
 * [ ] [ ] [w] [ ]
 * [ ] [ ] [b] [b]
 * [ ] [ ] [ ] [ ]
 * [p] [ ] [b] [ ]
 * [ ] [ ] [ ] [ ]
 * [ ] [ ] [b] [ ]
 * [ ] [ ] [t] [ ]
 */

public class BoulderTests {
    @Test
    @DisplayName("Test pushing a boulder into an empty space")
    public void testPushBoulderEmpty() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testBoulderPush", "c_staticTests_staticConfig");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        EntityResponse initBoulder = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(3, 1) && x.getType() == "boulder")).findFirst().get();
        
        EntityResponse expectedFirstPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 1), false);
        EntityResponse expectedSecondPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        EntityResponse expectedFirstBoulder = new EntityResponse(initBoulder.getId(), initBoulder.getType(), new Position(4, 1), false);
        EntityResponse expectedSecondBoulder = new EntityResponse(initBoulder.getId(), initBoulder.getType(), new Position(4, 2), false);

        // pushing the boulder at (1, 3)
        dmc.tick(Direction.RIGHT);
        DungeonResponse firstBoulderPushRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualFirstPlayer = getPlayer(firstBoulderPushRes).get();

        // checking player and boulder are in correct place
        assertEquals(expectedFirstPlayer, actualFirstPlayer);
        assertEquals(expectedFirstBoulder, getEntityFromID(firstBoulderPushRes, initBoulder.getId()).get());

        // test pushing same boulder second time, different direction
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        DungeonResponse secondBoulderPushRes = dmc.tick(Direction.DOWN);
        EntityResponse actualSecondPlayer = getPlayer(secondBoulderPushRes).get();

        // checking player and boulder are in correct place
        assertEquals(expectedSecondPlayer, actualSecondPlayer);
        assertEquals(expectedSecondBoulder, getEntityFromID(secondBoulderPushRes, initBoulder.getId()).get());

    }

    @Test
    @DisplayName("Test pushing boulder into second boulder")
    public void testDoubleBoulderPush() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testBoulderPush", "c_staticTests_staticConfig");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        EntityResponse initBoulder1 = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(3, -1) && x.getType() == "boulder")).findFirst().get();
        EntityResponse initBoulder2 = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(4, -1) && x.getType() == "boulder")).findFirst().get();
        
        // moving
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse postPushRes = dmc.tick(Direction.UP);

        assertEquals(initPlayer, getPlayer(postPushRes).get());
        assertEquals(initBoulder1, getEntityFromID(postPushRes, initBoulder1.getId()).get());
        assertEquals(initBoulder2, getEntityFromID(postPushRes, initBoulder2.getId()).get());
    }

    @Test
    @DisplayName("Test pushing boulder into wall")
    public void testBoulderWallPush() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testBoulderPush", "c_staticTests_staticConfig");
        EntityResponse initPlayer = getPlayer(initDungeonRes).get();
        EntityResponse initBoulder = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(3, -1) && x.getType() == "boulder")).findFirst().get();
        
        // moving
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        DungeonResponse postPushRes = dmc.tick(Direction.RIGHT);

        assertEquals(initPlayer, getPlayer(postPushRes).get());
        assertEquals(initBoulder, getEntityFromID(postPushRes, initBoulder.getId()).get());
    }

    @Test
    @DisplayName("Test pushing boulder onto collectible")
    public void testBoulderCollectiblePush() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_testBoulderPush", "c_staticTests_staticConfig");
        EntityResponse initBoulder = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(3, 1) && x.getType() == "boulder")).findFirst().get();
        EntityResponse initTreasure = initDungeonRes.getEntities().stream().filter(x->(x.getPosition() == new Position(3, -2) && x.getType() == "treasure")).findFirst().get();

        EntityResponse expectedBoulder = new EntityResponse(initBoulder.getId(), initBoulder.getType(), new Position(3, -2), false);


        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse finalDungeonRes = dmc.tick(Direction.DOWN);

        assertEquals(expectedBoulder, getEntityFromID(finalDungeonRes, initBoulder.getId()).get());
        assertEquals(initTreasure, getEntityFromID(finalDungeonRes, initTreasure.getId()).get());

    
    }
}
