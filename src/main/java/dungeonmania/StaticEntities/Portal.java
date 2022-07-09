package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private String colour;

    public Portal(String id, Position position, String colour) {
        super(id, position, false);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
}
