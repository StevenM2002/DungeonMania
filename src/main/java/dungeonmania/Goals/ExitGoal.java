package dungeonmania.Goals;

import dungeonmania.Player;
import dungeonmania.StaticEntities.Exit;
import java.util.List;
import dungeonmania.Entity;

public class ExitGoal implements Goal  {
    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return allEntities.stream()
            .filter(x->(x instanceof Exit))
            .map(x->(Exit) x)
            .anyMatch(x->x.getActivated());
    }
    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (hasCompleted(player, allEntities)) {
            return "";
        }
        return ":exit";
    }
}
