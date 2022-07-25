package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * [ ] [ ] [ ] [w] [ ]
 * [w] [p] [s] [m] [w]
 * [ ] [ ] [ ] [w] [ ]
 * swamp 
 */

public class SwampTests {
    @Test
    @DisplayName("Tests that the swamp tile affects the mercenary")
    public void testSwampMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("d_staticTests_swampTest", "c_staticTests_testSwampMercenary");
        EntityResponse initMerc = initDungeonRes.getEntities().stream().filter(x->x.getType().equals("mercenary")).findFirst().get();
        assertEquals(initMerc.getPosition(), new Position(2, 1));

        // mercenary gets stuck
        DungeonResponse initialTick = dmc.tick(Direction.LEFT);
        assertEquals(getEntityFromID(initialTick, initMerc.getId()).get().getPosition(), new Position(1, 1));

        // checking that its stuck for 2 ticks (movement factor)
        DungeonResponse firstStuckTick = dmc.tick(Direction.LEFT);
        assertEquals(getEntityFromID(firstStuckTick, initMerc.getId()).get().getPosition(), new Position(1, 1));
        DungeonResponse secondStuckTick = dmc.tick(Direction.LEFT);
        assertEquals(getEntityFromID(secondStuckTick, initMerc.getId()).get().getPosition(), new Position(1, 1));

        // checking that in frees from the swamp, walks into the player and dies
        DungeonResponse finalTick = dmc.tick(Direction.LEFT);
        assertTrue(!finalTick.getEntities().stream().anyMatch(x->x.getType().equals("mercenary")));
    }

    @Test
    @DisplayName("Tests that the player is not affected by swamp tiles")
    public void testSwampPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_staticTests_swampTest", "c_staticTests_testSwampMercenary");
        
        // check player moves onto the swamp
        DungeonResponse firstTick = dmc.tick(Direction.RIGHT);
        EntityResponse firstPlayer = getPlayer(firstTick).get();
        assertEquals(firstPlayer.getPosition(), new Position(1, 1));

        DungeonResponse secondTick = dmc.tick(Direction.RIGHT);
        EntityResponse secondPlayer = getPlayer(secondTick).get();
        assertEquals(secondPlayer.getPosition(), new Position(2, 1));
    }
}
