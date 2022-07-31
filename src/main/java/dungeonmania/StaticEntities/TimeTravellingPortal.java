package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class TimeTravellingPortal extends StaticEntity {

    public TimeTravellingPortal(String id, Position position) {
        super(id, position, false);
    }

    @Override
    public String getDefaultCollision() {
        return "Block";
    }
    
    
}
