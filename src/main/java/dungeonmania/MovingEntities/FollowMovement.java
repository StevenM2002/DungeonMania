package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.*;

public class FollowMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {
        Position relativePos = Position.calculatePositionBetween(entity.getPosition(), player.getPosition());
        var x = relativePos.getX();
        var y = relativePos.getY();
        List<Direction> queuePrio = Arrays.asList();
        if (x == 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
        }
        if (x == 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP);
        }
        if (y == 0 && x > 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.LEFT);
        }
        if (y == 0 && x < 0) {
            queuePrio = Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.UP, Direction.RIGHT);
        }
        if (x > 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.DOWN);
        }
        if (x > 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP);
        }
        if (x < 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
        }
        if (x < 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP);
        }

        Position initialPosition = entity.getPosition();

        // attempts to move in directions in order of queuePrio until it moves
        for (int i = 0; i < queuePrio.size() && entity.getPosition() == initialPosition; i++) {
            if (!entity.getPosition().translateBy(queuePrio.get(i)).equals(entity.getPreviousPosition())) {
                entity.move(queuePrio.get(i));
            }
        }
    }
}
