package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunningMovement implements Movement {
    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        var relativePos = getRelativePosition(currentPosition, player.getPosition());
        var directions = getPossibleDirectionsOfPos(relativePos);
        for (var direction : directions) {
            if (/*TODO Movement to this position is valid and it works*/) {
                return currentPosition.translateBy(direction);
            }
        }
        //TODO implement for if it cannot do any
        return currentPosition;
    }

    private Position getRelativePosition(Position runningAway, Position from) {
        // Center from on graph
        int x = from.getX() * -1;
        int y = from.getY() * -1;
        return runningAway.translateBy(x, y);
    }

    private List<Direction> getPossibleDirectionsOfPos(Position relativePosOfRunningAway) {
        int x = relativePosOfRunningAway.getX();
        int y = relativePosOfRunningAway.getY();
        if (x >= 0 && y >= 0) return Arrays.asList(Direction.UP, Direction.RIGHT);
        if (x >= 0 && y <= 0) return Arrays.asList(Direction.UP, Direction.LEFT);
        if (x <= 0 && y >= 0) return Arrays.asList(Direction.DOWN, Direction.RIGHT);
        return Arrays.asList(Direction.DOWN, Direction.LEFT);
    }

}
