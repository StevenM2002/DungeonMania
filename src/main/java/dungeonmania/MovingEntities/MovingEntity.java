package dungeonmania.MovingEntities;

import dungeonmania.CanMove;
import dungeonmania.Entity;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public abstract class MovingEntity extends Entity implements CanMove {
    private static double health;
    private static double attack;

    private Movement movementStrategy;
    private Position previousPosition;

    public void setHealth(double health) {
        this.health = health;
    }

    public static double getHealth() {
        return health;
    }

    public static double getAttack() {
        return attack;
    }

    public Movement getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(Movement movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public MovingEntity(String id, Position position, boolean isInteractable, double health, double attack,
                        Movement movementStrategy) {
        super(id, position, isInteractable);
        this.health = health;
        this.attack = attack;
        this.movementStrategy = movementStrategy;
    }
    public void doTickMovement() {
        movementStrategy.moveEntity(this);
    }

    @Override
    public void move(Direction direction) {
        Position tempPos = getPosition();
        collisionManager.requestMove(this, direction);
        if (tempPos != getPosition()) {
            previousPosition = tempPos;
        }
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }
}

