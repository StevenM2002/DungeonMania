package dungeonmania.StaticEntities;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;

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
    public EntityResponse getEntityResponse() {
        var type = getType() + "_" + colour;
        type = type.toLowerCase();
        System.out.println(type);
        System.out.println(UtilityFunctions.camelToSnake(type));
        return new EntityResponse(
                getId(),
                UtilityFunctions.camelToSnake(type),
                getPosition(),
                getIsInteractable());
    }
    @Override
    public String getDefaultCollision() {
        return "Block";
    }


    
}
