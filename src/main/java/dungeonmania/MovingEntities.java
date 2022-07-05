package dungeonmania;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class MovingEntities extends Entities {
    private double health;
    private double attack;
    private Movement movementStrategy;
    public void setMovementStrategy(Movement strategy) {
        this.movementStrategy = strategy;
    }
    public void doMovementStrategy(List<Entities> allEntities) {
        movementStrategy.doNextMove(allEntities);
    }
    public MovingEntities(String id, Position position, boolean isInteractable, Movement movementStrategy) {
        super(id, position, isInteractable);
        this.movementStrategy = movementStrategy;
    }
}
