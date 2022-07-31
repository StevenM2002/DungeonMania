package dungeonmania.MovingEntities;

import dungeonmania.Collisions.Block;
import dungeonmania.Collisions.CollisionManager;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.*;
import java.util.stream.Collectors;

import static dungeonmania.DungeonManiaController.getDmc;
import static dungeonmania.util.Direction.*;

public class FollowMovement extends Movement {
    List<Entity> blockingEntities = new ArrayList<>();
    @Override
    public void moveEntity(MovingEntity entity) {
        this.blockingEntities = getBlockingEntities(entity);
        Position positionToMoveTo = getNextPosition(getDmc().getAllEntities(), entity.getPosition());
        for (var direction : Arrays.asList(UP, DOWN, LEFT, RIGHT)) {
            if (positionToMoveTo.equals(entity.getPosition().translateBy(direction))) {
                entity.move(direction);
                return;
            }
        }
    }

    private List<Entity> getBlockingEntities(Entity entity) {
        return getDmc().getAllEntities().stream().filter(it -> CollisionManager.getCollision(entity, it) instanceof Block).collect(Collectors.toList());
    }

    public Position getNextPosition(List<Entity> allEntities, Position currentPosition) {
        Player player = getDmc().getPlayer();
        if (player == null) return currentPosition;
        var nextPos = doDijkstraGetNextPos(allEntities, currentPosition, player);
        if (nextPos != null) {
            return nextPos;
        }
        return currentPosition;
    }

    private List<Position> getValidAdjacentPositions(ArrayList<Position> grid, List<Entity> blockingEntities, Position currPos, Set<Position> notVisited) {
        HashSet<Position> retPos = new HashSet<>();
        List<Direction> directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        var portalEntity = getDmc().getAllEntities().stream().filter(it -> it instanceof Portal && it.getPosition().equals(currPos)).findAny().orElse(null);
        if (portalEntity != null) {
            retPos.add(((Portal) portalEntity).getOtherPortal().getPosition());
        }
        for (var direction : directions) {
            Position nextPosition = currPos.translateBy(direction);
            if (!blockingEntities.stream().anyMatch(entities -> entities.getPosition().equals(nextPosition)) && isInBounds(grid, currPos) && notVisited.contains(nextPosition)) {
                retPos.add(nextPosition);
            }
        }
        return new ArrayList<>(retPos);
    }

    private Position getFirstPosition(HashMap<Position, Position> lastVisited, Position source, Position dest) {
        // If there is no way to the source then null
        if (lastVisited.get(dest) == null) return null;
        Position nextPos = lastVisited.get(dest);
        if (nextPos.equals(source)) return dest;
        while (lastVisited.get(nextPos) != source) {
            nextPos = lastVisited.get(nextPos);
        }
        return nextPos;
    }

    private Position doDijkstraGetNextPos(List<Entity> allEntities, Position source, Entity dest) {
        ArrayList<Position> grid = getDFSGrid(allEntities);
        HashSet<Position> notVisited = new HashSet<>(grid);
        HashMap<Position, Integer> distance = new HashMap<>();
        // Position after movement, Previous position
        HashMap<Position, Position> lastVisited = new HashMap<>();
        for (Position position : grid) {
            distance.put(position, Integer.MAX_VALUE);
            lastVisited.put(position, null);
        }
        distance.put(source, 0);
        Position v = source;
        List<Entity> blockingEntities = this.blockingEntities;
        for (int numNotVisited = notVisited.size() - 1; numNotVisited > 0; numNotVisited--) {
            notVisited.remove(v);
            if (v != null) {
                for (Position w : getValidAdjacentPositions(grid, blockingEntities, v, notVisited)) {
                    int weight = 1;
                    Position finalV = v;
                    Entity entityAtPos = getDmc()
                            .getAllEntities()
                            .stream()
                            .filter(entity -> entity instanceof SwampTile && entity.getPosition().equals(finalV))
                            .findAny()
                            .orElse(null);
                    if (entityAtPos != null) {
                        weight = ((SwampTile) entityAtPos).getMovementFactor() + 1;
                    }
                    int newDist = distance.get(v) + weight;
                    if (newDist < distance.get(w)) {
                        distance.put(w, newDist);
                        lastVisited.put(w, v);
                    }
                }
            }
            v = getNextV(distance, notVisited);
        }
        return getFirstPosition(lastVisited, source, dest.getPosition());
    }

    public Position getNextV(HashMap<Position, Integer> distance, HashSet<Position> notVisited) {
        Position minPos = null;
        int minInt = Integer.MAX_VALUE;
        for (Position position : notVisited) {
            if (distance.get(position) < minInt) {
                minInt = distance.get(position);
                minPos = position;
            }
        }
        return minPos;
    }

    private ArrayList<Position> getDFSGrid(List<Entity> allEntities) {
        ArrayList<Position> grid = new ArrayList<>();
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (Entity entity : allEntities) {
            int xPos = entity.getPosition().getX();
            int yPos = entity.getPosition().getY();
            maxX = Math.max(xPos, maxX);
            maxY = Math.max(yPos, maxY);
            minX = Math.min(xPos, minX);
            minY = Math.min(yPos, minY);
        }
        maxX++; maxY++; minX--; minY--;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                grid.add(new Position(i, j));
            }
        }
        return grid;
    }

    private boolean isInBounds(ArrayList<Position> grid, Position pos) {
        return grid.contains(pos);
    }
}
