package dungeonmania.MovingEntities;

import dungeonmania.CanMove;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements CanMove {
    private double health;
    private double attack;

    private Movement movementStrategy;

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealth() {
        return health;
    }

    public double getAttack() {
        return attack;
    }

    public void setMovementStrategy(Movement movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public MovingEntity(String id, Position position, boolean isInteractable, double health, double attack,
                        Movement movementStrategy) {
        super(id, position, false);
        this.health = health;
        this.attack = attack;
        this.movementStrategy = movementStrategy;
    }
    public void move(Player player) {
        this.setPosition(movementStrategy.getNextPosition(player, this.getPosition()));
    }
    @Override
    public void move(Direction direction) {
        collisionManager.requestMove(this, direction);
    }
}
