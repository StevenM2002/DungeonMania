package dungeonmania;

import dungeonmania.Collisions.CollisionManager;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;

public abstract class Entity {
    private String id;
    private Position position;
    private boolean isInteractable;
    public static CollisionManager collisionManager;

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

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(
            this.id, 
            UtilityFunctions.camelToSnake(this.getType()),
            position,
            isInteractable);
    }

    /**
     * returns the name of the default collision type when something collides 
     * with this entity. This can be overridden in collisionManager
     * @return
     */
    public String getDefaultCollision() {
        return "Pass";
    }
    public boolean getIsInteractable() {
        return isInteractable;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            Entity e = (Entity) obj;
            return e.getId() == this.getId();
        }
        return false;
    }
}
