package dungeonmania;

import java.util.List;

public interface Movement {
    void doNextMove(List<Entities> allEntities);
}
