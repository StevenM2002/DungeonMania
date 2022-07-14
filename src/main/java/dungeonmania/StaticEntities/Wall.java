package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Wall extends StaticEntity {
    public Wall(String id, Position position) {
        super(id, position, false);
    }

    @Override
    public String getDefaultCollision() {
        return "Block";
    }

    
}