package dungeonmania;

import dungeonmania.util.Position;

public class Wall extends StaticEntities {
    public Wall(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}