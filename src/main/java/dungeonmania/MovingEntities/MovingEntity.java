package dungeonmania.MovingEntities;

import dungeonmania.CanMove;
import dungeonmania.Entity;
import dungeonmania.Collisions.CollisionManager;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public abstract class MovingEntity extends Entity implements CanMove, Battling {
    private double health;
    private double attack;

    private Movement movementStrategy;
    private Position previousPosition;
    private int stuckAmount = 0;

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealth() {
        return health;
    }

    public Movement getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(Movement movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public MovingEntity(String id, Position position, boolean isInteractable, double health, double attack, Movement movementStrategy) {
        super(id, position, isInteractable);
        this.health = health;
        this.attack = attack;
        this.movementStrategy = movementStrategy;
    }
    public void doTickMovement() {
        if (stuckAmount > 0) {
            stuckAmount -= 1;
        } else {
            movementStrategy.moveEntity(this);
        }
    }

    @Override
    public void move(Direction direction) {
        CollisionManager.requestMove(this, direction);
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    @Override
    public double takeDamage(double damage) {
        double damageTaken = damage / 5;
        // if (health - damageTaken < 0) {
        //     damageTaken = health;
        // }
        health -= damageTaken;
        return damageTaken;
    }

    @Override
    public double dealDamage() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void setPreviousPosition(Position previousPosition) {
        this.previousPosition = previousPosition;
    }

    public void setStuckAmount(int stuckAmount) {
        this.stuckAmount = stuckAmount;
    }
}

