package dungeonmania;

import dungeonmania.util.Position;

import java.util.List;

public interface Movement {
    Position getNextPosition(List<Entities> allEntities, Position currentPosition);
}
