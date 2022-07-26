package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.*;

public class FollowMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {

    }

    /**
     * Returns a non-directional and weighted adjacency matrix
     * @return Adjacency matrix accessed with Positions where if Integer is null then it is not adjacent
     */
    private HashMap<Position, HashMap<Position, Integer>> getAdjacencyMatrix() {
        // Create a restricted zone
        // Remove all blocking nodes from this restricted zone
        // In this restricted zone, add all floor tile nodes to the adjacency matrix
        // Add all portal nodes to this adjacency matrix
        // Add all swamp tiles to this adjacency matrix
        return null;
    }


//    @Override
//    public void moveEntity(MovingEntity entity) {
//        Position relativePos = Position.calculatePositionBetween(entity.getPosition(), player.getPosition());
//        var x = relativePos.getX();
//        var y = relativePos.getY();
//        List<Direction> queuePrio = Arrays.asList();
//        if (x == 0 && y < 0) {
//            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
//        }
//        if (x == 0 && y > 0) {
//            queuePrio = Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP);
//        }
//        if (y == 0 && x > 0) {
//            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.LEFT);
//        }
//        if (y == 0 && x < 0) {
//            queuePrio = Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.UP, Direction.RIGHT);
//        }
//        if (x > 0 && y < 0) {
//            queuePrio = Arrays.asList(Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.DOWN);
//        }
//        if (x > 0 && y > 0) {
//            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP);
//        }
//        if (x < 0 && y < 0) {
//            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
//        }
//        if (x < 0 && y > 0) {
//            queuePrio = Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP);
//        }
//
//        Position initialPosition = entity.getPosition();
//
//        // attempts to move in directions in order of queuePrio until it moves
//        for (int i = 0;
//            i < queuePrio.size() && entity.getPosition() == initialPosition;
//            i++
//        ) {
//            entity.move(queuePrio.get(i));
//        }
//    }
}
