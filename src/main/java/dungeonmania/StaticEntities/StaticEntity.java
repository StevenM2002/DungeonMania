package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {
    public StaticEntity(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}