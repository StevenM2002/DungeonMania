package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.List;
import static dungeonmania.DungeonManiaController.getDmc;


public class RunningMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {
        Position relativePositionOfPlayerToEntity = Position.calculatePositionBetween(getDmc().getPlayer().getPosition(), entity.getPosition());
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
}
