package dungeonmania;

import dungeonmania.util.Position;

public abstract class MovingEntities extends Entities {
    private double health;
    private double attack;
    private Movement movementStrategy;
    public void setMovementStrategy(Movement strategy) {
        this.movementStrategy = strategy;
    }
    public void doMovementStrategy() {
        movementStrategy.doNextMove();
    }
    public MovingEntities(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}
