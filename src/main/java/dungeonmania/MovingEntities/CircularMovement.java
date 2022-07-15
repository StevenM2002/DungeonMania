package dungeonmania.MovingEntities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;


public class CircularMovement extends Movement {
    private boolean isReversed = false;
    private Position pivot = null;

    @Override
    public void moveEntity(MovingEntity entity) {
        if (pivot == null) {
            pivot = entity.getPosition();
            entity.move(Direction.UP);
            return;
        }
        Position previousPos = entity.getPosition();
        moveEntityCircular(entity);
        if (entity.getPosition() == previousPos) {
            isReversed = !isReversed;
            moveEntityCircular(entity);
        }
    }

    private void moveEntityCircular(MovingEntity entity) {
        int index = pivot.getAdjacentPositions().indexOf(entity.getPosition());
        if (isReversed) {
            iterateIndex(index, -1, pivot.getAdjacentPositions().size());
        } else {
            iterateIndex(index, 1, pivot.getAdjacentPositions().size());
        }
        Direction direction = entity.getPosition().getDirectionTo(pivot.getAdjacentPositions().get(index));
        entity.move(direction);
    }

    private int iterateIndex(int index, int iteration, int maxSize) {
        index += iteration;
        if (index >= maxSize) {
            index = 0;
        } else if (index < 0) {
            index = maxSize - 1;
        }
        return index;
    }
}
