package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;

    public int getMovementFactor() {
        return movementFactor;
    }

    public SwampTile(String id, Position position, int movementFactor) {
        super(id, position, false);
        this.movementFactor = movementFactor;
    }

    @Override
    public String getDefaultCollision() {
        return "Stuck";
    }
    
}
