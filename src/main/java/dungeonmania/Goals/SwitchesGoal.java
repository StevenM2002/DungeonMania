package dungeonmania.Goals;

import dungeonmania.Player;
import dungeonmania.StaticEntities.FloorSwitch;

import java.util.List;
import dungeonmania.Entity;

public class SwitchesGoal implements Goal{

    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return allEntities.stream()
            .filter(x->(x instanceof FloorSwitch))
            .map(x->(FloorSwitch) x)
            .allMatch(x->x.getActivated());
    }
    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (hasCompleted(player, allEntities)) {
            return "";
        }
        return ":boulders";
    }
}
