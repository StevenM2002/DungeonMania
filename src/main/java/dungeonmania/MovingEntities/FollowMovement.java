package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.*;

public class FollowMovement implements Movement {
    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        var relativePos = getRelativePositionOfPlayer(currentPosition, player.getPosition());
        var x = relativePos.getX();
        var y = relativePos.getY();
        List<Direction> queuePrio = Arrays.asList();
        if (x == 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
        }
        if (x == 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP);
        }
        if (y == 0 && x > 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.LEFT);
        }
        if (y == 0 && x < 0) {
            queuePrio = Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.UP, Direction.RIGHT);
        }
        if (x > 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.DOWN);
        }
        if (x > 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP);
        }
        if (x < 0 && y > 0) {
            queuePrio = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
        }
        if (x < 0 && y < 0) {
            queuePrio = Arrays.asList(Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP);
        }
        for (var direction : queuePrio) {
            if (/*TODO Movement to this position is valid and it works*/) {
                return currentPosition.translateBy(direction);
            }
        }
        //TODO implement for if it cannot do anything
        return currentPosition;
    }
    private Position getRelativePositionOfPlayer(Position mercenary, Position player) {
        int x = mercenary.getX() * -1;
        int y = mercenary.getY() * -1;
        return player.translateBy(x, y);
    }
//    @Override
//    public Position getNextPosition(List<Entity> allEntities, Position currentPosition) {
//        Player player = (Player) allEntities.stream().filter(entities -> entities.getClass().getSimpleName().equalsIgnoreCase("player")).findFirst().orElse(null);
//        if (player == null) return currentPosition;
//        var nextPos = doDijkstraGetNextPos(allEntities, currentPosition, player);
//        if (nextPos != null) {
//            return nextPos;
//        }
//        return getAlternatePosition(getBlockingEntities(allEntities), currentPosition, player.getPosition());
//    }
//
//    private Position getAlternatePosition(List<Entity> blockingEntities, Position currPos, Position endPos) {
//        var directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
//        for (var direction : directions) {
//            var nextPos = currPos.translateBy(direction);
//            if (!blockingEntities.stream().anyMatch(entity -> entity.getPosition().equals(nextPos))) {
//                return nextPos;
//            }
//        }
//        return currPos;
//    }
//
//    private List<Position> getValidAdjacentPositions(ArrayList<Position> grid, List<Entity> blockingEntities, Position currPos, Set<Position> notVisited) {
//        List<Position> retPos = new ArrayList<>();
//        List<Direction> directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
//        for (var direction : directions) {
//            Position nextPosition = currPos.translateBy(direction);
//            if (!blockingEntities.stream().anyMatch(entities -> entities.getPosition().equals(nextPosition)) && isInBounds(grid, currPos) && notVisited.contains(nextPosition)) {
//                retPos.add(nextPosition);
//            }
//        }
//        return retPos;
//    }
//
//    private Position getFirstPosition(HashMap<Position, Position> lastVisited, Position source, Position dest) {
//        // If there is no way to the source then null
//        if (lastVisited.get(dest) == null) return null;
//        Position nextPos = lastVisited.get(dest);
//        while (lastVisited.get(nextPos) != source) {
//            nextPos = lastVisited.get(nextPos);
//        }
//        return nextPos;
//    }
//
//    private Position doDijkstraGetNextPos(List<Entity> allEntities, Position source, Entity dest) {
//        ArrayList<Position> grid = getDFSGrid(allEntities);
//        HashSet<Position> notVisited = new HashSet<>(grid);
//        HashMap<Position, Integer> distance = new HashMap<>();
//        // Position after movement, Previous position
//        HashMap<Position, Position> lastVisited = new HashMap<>();
//        for (Position position : grid) {
//            distance.put(position, Integer.MAX_VALUE);
//            lastVisited.put(position, null);
//        }
//        distance.put(source, 0);
//        Position v = source;
//        List<Entity> blockingEntities = getBlockingEntities(allEntities);
//        for (int numNotVisited = notVisited.size() - 1; numNotVisited > 0; numNotVisited--) {
//            notVisited.remove(v);
//            if (v != null) {
//                for (Position w : getValidAdjacentPositions(grid, blockingEntities, v, notVisited)) {
//                    int newDist = distance.get(v) + 1;
//                    if (newDist < distance.get(w)) {
//                        distance.put(w, newDist);
//                        lastVisited.put(w, v);
//                    }
//                }
//            }
//            v = getNextV(distance, notVisited);
//        }
//        return getFirstPosition(lastVisited, source, dest.getPosition());
//    }
//
//    public Position getNextV(HashMap<Position, Integer> distance, HashSet<Position> notVisited) {
//        Position minPos = null;
//        int minInt = Integer.MAX_VALUE;
//        for (Position position : notVisited) {
//            if (distance.get(position) < minInt) {
//                minInt = distance.get(position);
//                minPos = position;
//            }
//        }
//        return minPos;
//    }
//
//    private ArrayList<Position> getDFSGrid(List<Entity> allEntities) {
//        ArrayList<Position> grid = new ArrayList<>();
//        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
//        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
//        for (Entity entity : allEntities) {
//            int xPos = entity.getPosition().getX();
//            int yPos = entity.getPosition().getY();
//            maxX = Math.max(xPos, maxX);
//            maxY = Math.max(yPos, maxY);
//            minX = Math.min(xPos, minX);
//            minY = Math.min(yPos, minY);
//        }
//        maxX++; maxY++; minX--; minY--;
//        for (int i = minX; i <= maxX; i++) {
//            for (int j = minY; j <= maxY; j++) {
//                grid.add(new Position(i, j));
//            }
//        }
//        return grid;
//    }
//
//    private boolean isInBounds(ArrayList<Position> grid, Position pos) {
//        return grid.contains(pos);
//    }
}
