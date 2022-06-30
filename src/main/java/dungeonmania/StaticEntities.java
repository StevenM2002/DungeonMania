package dungeonmania;

import dungeonmania.util.Position;

public abstract class StaticEntities extends Entities {
    public StaticEntities(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}