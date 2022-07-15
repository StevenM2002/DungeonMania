package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;

public class RandomMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {
        entity.move(getRandomDirection());        
    }

    private Direction getRandomDirection() {
        java.util.Random rand = new java.util.Random();
        int randNum = rand.nextInt(4) + 1;
        if (randNum == 1) return Direction.RIGHT;
        if (randNum == 2) return Direction.UP;
        if (randNum == 3) return Direction.LEFT;
        return Direction.DOWN;
    }
}
