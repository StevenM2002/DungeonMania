package dungeonmania;

import dungeonmania.util.Position;

public class Portal extends StaticEntities {
    private String colour;

    public Portal(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    public String getColour() {
        return colour;
    }
}
