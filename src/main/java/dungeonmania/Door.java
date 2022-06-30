package dungeonmania;

import dungeonmania.util.Position;

public class Door extends StaticEntities {
    private Key key;

    public Door(Key key, String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
        this.key.setDoor(this);
    }

}