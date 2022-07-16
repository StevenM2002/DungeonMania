package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.List;

public class RunningMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {
//        List<Direction> directions = getPossibleDirectionsOfPos(Position.calculatePositionBetween(player.getPosition(), entity.getPosition()));
//        System.out.println(directions);
        Position relativePositionOfPlayerToEntity = Position.calculatePositionBetween(player.getPosition(), entity.getPosition());
        int x = relativePositionOfPlayerToEntity.getX();
        int y = relativePositionOfPlayerToEntity.getY();
        List<Direction> directions = Arrays.asList();
        if (x == 0 && y < 0) {
            directions = Arrays.asList(Direction.UP, Direction.LEFT, Direction.RIGHT);
        }
        if (x == 0 && y > 0) {
            directions = Arrays.asList(Direction.DOWN, Direction.RIGHT, Direction.LEFT);
        }
        if (y == 0 && x > 0) {
            directions = Arrays.asList(Direction.RIGHT, Direction.DOWN, Direction.UP);
        }
        if (y == 0 && x < 0) {
            directions = Arrays.asList(Direction.LEFT, Direction.DOWN, Direction.UP);
        }
        if (x > 0 && y < 0) {
            directions = Arrays.asList(Direction.RIGHT, Direction.UP);
        }
        if (x > 0 && y > 0) {
            directions = Arrays.asList(Direction.RIGHT, Direction.DOWN);
        }
        if (x < 0 && y < 0) {
            directions = Arrays.asList(Direction.UP, Direction.LEFT);
        }
        if (x < 0 && y > 0) {
            directions = Arrays.asList(Direction.DOWN, Direction.LEFT);
        }

        Position initialPosition = entity.getPosition();
        for (int i = 0; 
            i < directions.size() 
            && entity.getPosition() == initialPosition;
            i++
        ) {
            entity.move(directions.get(i));
        }
    }

    // private List<Direction> getPossibleDirectionsOfPos(Position relativePosOfRunningAway) {
    //     int x = relativePosOfRunningAway.getX();
    //     int y = relativePosOfRunningAway.getY();
    //     if (x >= 0 && y <= 0) return Arrays.asList(Direction.UP, Direction.RIGHT);
    //     if (x >= 0 && y >= 0) return Arrays.asList(Direction.UP, Direction.LEFT);
    //     if (x <= 0 && y <= 0) return Arrays.asList(Direction.DOWN, Direction.RIGHT);
    //     return Arrays.asList(Direction.DOWN, Direction.LEFT);
    // }

}
