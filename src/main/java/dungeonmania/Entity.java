package dungeonmania;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;

public abstract class Entity {
    private String id;
    private Position position;
    private boolean isInteractable;

    public Entity(String id, Position position, boolean isInteractable) {
        this.id = id;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(
            this.id, 
            UtilityFunctions.camelToSnake(this.getClass().getSimpleName()),
            position,
            isInteractable);
    }
}
