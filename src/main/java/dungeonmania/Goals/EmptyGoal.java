package dungeonmania.Goals;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Player;

public class EmptyGoal implements Goal {

    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        return "";
    }

    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return false;
    }
    
}
