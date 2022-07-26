package dungeonmania.MovingEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.util.Position;
import java.util.*;
import java.util.stream.Collectors;

public class FollowMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {

    }

    /**
     * Returns a directional and weighted adjacency matrix where swamp tile to non-swamp tile is 2 and tile to swamp tile is 1
     * HashMap<From, Hashmap<To, Weight>>
     * @return Adjacency matrix accessed with Positions where if Integer is null then it is not adjacent
     */
    private HashMap<Position, HashMap<Position, Integer>> getAdjacencyMatrix(List<Entity> allEntities) {
        // Create a restricted zone
        var restrictedZone = getRestrictedZone(allEntities);
        var entitiesNotInMatrix = new ArrayList<>(allEntities);
        var positionsOfEntitiesNotInMatrix = entitiesNotInMatrix.stream().map(entity -> entity.getPosition()).collect(Collectors.toList());
        // String through all entities in restricted zone and add to adjacency matrix
        var ret = new HashMap<Position, HashMap<Position, Integer>>();


        // if Swamp Tile
        // if Portal node
        // if Blocking node
        // else free to move node
        // Remove all blocking nodes from this restricted zone
        // In this restricted zone, add all floor tile nodes to the adjacency matrix
        // Add all portal nodes to this adjacency matrix
        // Add all swamp tiles to this adjacency matrix
        return null;
    }



    private Entity getEntityInPosition(Position position) {
        return DungeonManiaController
                .getDmc()
                .getAllEntities()
                .stream()
                .filter(entity -> entity.getPosition().equals(position))
                .findAny()
                .orElse(null);
    }

    private List<Position> getRestrictedZone(List<Entity> allEntities) {
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
}
