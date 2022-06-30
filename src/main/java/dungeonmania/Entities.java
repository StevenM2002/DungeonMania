package dungeonmania;

import dungeonmania.util.Position;

public abstract class Entities {
    private String id;
    private Position position;
    private boolean isInteractable;

    public Entities(String id, Position position, boolean isInteractable) {
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

}
