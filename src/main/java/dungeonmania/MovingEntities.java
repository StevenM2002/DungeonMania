package dungeonmania;

import dungeonmania.util.Position;

public abstract class MovingEntities extends Entities {
    private double health;
    private double attack;

    public MovingEntities(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}
