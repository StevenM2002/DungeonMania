package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private String colour;
    private Portal otherPortal;

    public Portal getOtherPortal() {
        return otherPortal;
    }

    public void setOtherPortal(Portal otherPortal) {
        this.otherPortal = otherPortal;
    }    

    public Portal(String id, Position position, String colour) {
        super(id, position, false);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
    @Override
    public String getDefaultCollision() {
        return "Block";
    }


    
}
