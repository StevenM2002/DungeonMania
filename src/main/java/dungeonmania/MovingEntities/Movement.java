package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Collision;
import dungeonmania.util.Position;

import java.util.List;
import java.util.stream.Collectors;

public interface Movement {
    Position getNextPosition(Player player, Position currentPosition);

    default List<Entity> getBlockingEntities(List<Entity> allEntities) {
        //TODO change this so its actually working
        return allEntities.stream().filter(entity -> {
            if (entity instanceof Collision) {
                return ((Collision) entity).isBlocking();
            }
            return false;
        }).collect(Collectors.toList());
    }
}
