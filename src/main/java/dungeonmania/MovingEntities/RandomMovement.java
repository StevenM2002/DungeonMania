package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;

public class RandomMovement implements Movement {

    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        Position nextPosition = currentPosition.translateBy(getRandomDirection());
        return nextPosition;
    }

    private Direction getRandomDirection() {
        java.util.Random rand = new java.util.Random();
        int randNum = rand.nextInt(4) + 1;
        if (randNum == 1) return Direction.RIGHT;
        if (randNum == 2) return Direction.UP;
        if (randNum == 3) return Direction.LEFT;
        return Direction.RIGHT;
    }
}
