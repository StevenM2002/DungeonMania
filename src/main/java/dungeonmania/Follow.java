package dungeonmania;

import dungeonmania.util.Position;

import java.util.List;

public class Follow implements Movement {
    @Override
    public Position getNextPosition(List<Entities> allEntities, Position currentPosition) {
        return currentPosition;

    }
}
