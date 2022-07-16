package dungeonmania.Goals;

import dungeonmania.Player;
import java.util.List;
import dungeonmania.Entity;

public interface Goal {
    public abstract Boolean hasCompleted(Player player, List<Entity> allEntities);
    public abstract String getTypeString(Player player, List<Entity> allEntities);
}
