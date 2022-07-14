package dungeonmania.StaticEntities;

import dungeonmania.CanMove;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity implements CanMove {
    public Boulder(String id, Position position) {
        super(id, position, false);
    }

    @Override
    public void move(Direction direction) {
        collisionManager.requestMove(this, direction);
    }

    @Override
    public String getDefaultCollision() {
        return "Block";
    }

    
}