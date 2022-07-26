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
import java.util.stream.Stream;

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
        System.out.println("FOLLOW MOVEMENT LINE 30");
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
//        return getAlternatePosition(blockingEntities, currentPosition, player.getPosition());
    }

    private Position getAlternatePosition(List<Entity> blockingEntities, Position currPos, Position endPos) {
        var directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        for (var direction : directions) {
            var nextPos = currPos.translateBy(direction);
            if (!blockingEntities.stream().anyMatch(entity -> entity.getPosition().equals(nextPos))) {
                return nextPos;
            }
        }
        return currPos;
    }

    private List<Position> getValidAdjacentPositions(ArrayList<Position> grid, List<Entity> blockingEntities, Position currPos, Set<Position> notVisited) {
        List<Position> retPos = new ArrayList<>();
        List<Direction> directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        for (var direction : directions) {
            Position nextPosition = currPos.translateBy(direction);
            if (!blockingEntities.stream().anyMatch(entities -> entities.getPosition().equals(nextPosition)) && isInBounds(grid, currPos) && notVisited.contains(nextPosition)) {
                retPos.add(nextPosition);
            }
        }
        return retPos;
    }

    private Position getFirstPosition(HashMap<Position, Position> lastVisited, Position source, Position dest) {
        // If there is no way to the source then null
        if (lastVisited.get(dest) == null) return null;
        Position nextPos = lastVisited.get(dest);
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
                        weight = ((SwampTile) entityAtPos).getMovementFactor();
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
//    @Override
//    public void moveEntity(MovingEntity entity) {
//        System.out.println(getAdjacencyMatrix(entity));
//    }
//
//    /**
//     * Returns a directional and weighted adjacency list where swamp tile to non-swamp tile is 2 and tile to swamp tile is 1
//     * HashMap<From, List<Hashmap<To, Weight>>>
//     * @return Adjacency matrix accessed with Positions where if Integer is null then it is not adjacent
//     */
//    private HashMap<Position, List<HashMap<Position, Integer>>> getAdjacencyMatrix(MovingEntity thisEntity) {
//        var allEntities = getDmc()
//                .getAllEntities()
//                .stream()
//                .filter(entity -> !(entity instanceof Player))
//                .collect(Collectors.toList());
//        // Create a restricted zone
//        var restrictedZone = getRestrictedZone(getDmc().getAllEntities());
//        // String through all entities in restricted zone and add to adjacency matrix
//        var matrix = new HashMap<Position, List<HashMap<Position, Integer>>>();
//        // Add everything as normal tiles
//        for (var fromPos : restrictedZone) {
//            for (var toPos : restrictedZone) {
//                var temp = new HashMap<Position, Integer>();
//                temp.put(toPos, 1);
//                try {
//                    var adjacencyList = matrix.get(fromPos);
//                    adjacencyList.add(temp);
//                } catch (Exception e /*TODO change to more specific exception*/) {
//                    matrix.put(fromPos, new ArrayList<>());
//                    var adjacencyList = matrix.get(fromPos);
//                    adjacencyList.add(temp);
//                }
//            }
//        }
//        // Add swamp tiles
//        var positionsOfSwampTiles = allEntities
//                .stream()
//                .filter(entity -> entity instanceof SwampTile)
//                .map(Entity::getPosition)
//                .collect(Collectors.toList());
//        for (var fromPos : positionsOfSwampTiles) {
//            for (var toPos : getCardinallyAdjacentValidPos(restrictedZone, fromPos)) {
//                var innerMap = matrix.get(fromPos);
//                var tempMap = new HashMap<Position, Integer>();
//                tempMap.put(toPos, ((SwampTile) getEntityInPosition(fromPos)).getMovementFactor());
//                innerMap.add(tempMap);
//            }
//        }
//        // Add portal nodes
//        // List<Map<From, To>>
//        var mapOfMatchingPortals = new ArrayList<HashMap<Portal, Portal>>();
//        allEntities
//                .stream()
//                .filter(entity -> entity instanceof Portal)
//                .map(entity -> ((Portal) entity))
//                .forEach(portal -> {
//                    var tempMap = new HashMap<Portal, Portal>();
//                    tempMap.put(portal, portal.getOtherPortal());
//                    mapOfMatchingPortals.add(tempMap);
//                });
//        for (var mapOfMatchingPortal : mapOfMatchingPortals) {
//            for (var entrySet : mapOfMatchingPortal.entrySet()) {
//                var innerMap = matrix.get(entrySet.getKey().getPosition());
//                var tempMap = new HashMap<Position, Integer>();
//                tempMap.put(entrySet.getValue().getPosition(), 1);
//                innerMap.add(tempMap);
//            }
//        }
//        // Add Blockings
//        var positionOfBlocking = allEntities
//                .stream()
//                .filter(it -> CollisionManager.getCollision(thisEntity, it) instanceof Block)
//                .map(Entity::getPosition)
//                .collect(Collectors.toList());
//
//        for (var toPositionsList : matrix.values()) {
//            for (var toPositions : toPositionsList) {
//                for (var entrySet : toPositions.entrySet()) {
//                    if (positionOfBlocking.contains(entrySet.getKey())) {
//                        toPositions.put(entrySet.getKey(), null);
//                    }
//                }
//            }
//        }
//        for (var entrySet : matrix.entrySet()) {
//            if (positionOfBlocking.contains(entrySet.getKey())) {
//                matrix.put(entrySet.getKey(), new ArrayList<>());
//            }
//        }
//        return matrix;
//    }
//
//    private List<Position> getCardinallyAdjacentValidPos(List<Position> restrictedZone, Position position) {
//        return Stream.of(UP, DOWN, LEFT, RIGHT)
//                .map(position::translateBy)
//                .filter(restrictedZone::contains)
//                .collect(Collectors.toList());
//    }
//    // Will not get the player entity and instead return null
//    private Entity getEntityInPosition(Position position) {
//        return getDmc()
//                .getAllEntities()
//                .stream()
//                .filter(entity -> entity.getPosition().equals(position) && !(entity instanceof Player))
//                .findAny()
//                .orElse(null);
//    }
//
//    private List<Position> getRestrictedZone(List<Entity> allEntities) {
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
}
