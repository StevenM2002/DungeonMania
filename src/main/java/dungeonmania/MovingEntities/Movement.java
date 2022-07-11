package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Collision;
import dungeonmania.util.Position;

import java.util.List;
import java.util.stream.Collectors;

public interface Movement {
    Position getNextPosition(List<Entity> allEntities, Position currentPosition);

    default List<Entity> getBlockingEntities(List<Entity> allEntities) {
        return allEntities.stream().filter(entity -> {
            if (entity instanceof Collision) {
                return ((Collision) entity).isBlocking();
            }
            return false;
        }).collect(Collectors.toList());
    }
}
