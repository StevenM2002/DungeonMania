package dungeonmania;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.stream.Collectors;

public class Random implements Movement {
    @Override
    public Position getNextPosition(List<Entities> allEntities, Position currentPosition) {
        Position nextPosition = currentPosition.translateBy(getRandomDirection());
        if (getBlockingEntities(allEntities).stream().anyMatch(entities -> entities.getPosition().equals(nextPosition))) {
            return currentPosition;
        }
        return nextPosition;
    }

    private List<Entities> getBlockingEntities(List<Entities> allEntities) {
        return allEntities.stream().filter(entity -> entity instanceof Collision).collect(Collectors.toList());
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
