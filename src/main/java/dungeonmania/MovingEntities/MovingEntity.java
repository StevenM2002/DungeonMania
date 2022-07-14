package dungeonmania.MovingEntities;

import dungeonmania.CanMove;
import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements CanMove {
    private double health;
    private double attack;

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealth() {
        return health;
    }

    public double getAttack() {
        return attack;
    }

    public MovingEntity(String id, Position position, boolean isInteractable, double health, double attack) {
        super(id, position, false);
        this.health = health;
        this.attack = attack;
    }

    @Override
    public void move(Direction direction) {
        collisionManager.requestMove(this, direction);
    }
}
