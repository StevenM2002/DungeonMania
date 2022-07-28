package dungeonmania.staticEntityTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getGoals;

public class ExitTests {
    @Test
    @DisplayName("Test that the game is won when the exit is entered")
    public void testExitWin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testExitWin", "c_staticTests_staticConfig");
        assertTrue(getGoals(res).contains(":exit"));
        
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":exit"));
        
        // move to exit
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test moving into the exit when exit is not the goal")
    public void testNotExitGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testNotExitGoal", "c_staticTests_staticConfig");
        assertTrue(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":exit"));
        
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":exit"));
        
    
        // move to exit
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":exit"));
    }

    @Test 
    @DisplayName("Test try exit before completing sub goals")
    public void testComplexExitGoal()  {
        //NOTE: This test requires treasure to be implemented
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_staticTests_testComplexExitGoal", "c_staticTests_staticConfig");
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        
        dmc.tick(Direction.DOWN);
        // move to exit without collecting treasure
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.UP);
        assertTrue(getGoals(res).contains(":exit"));
        assertFalse(getGoals(res).contains(":treasure"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(res));

    }

}
