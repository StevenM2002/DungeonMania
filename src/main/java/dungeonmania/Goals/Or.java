package dungeonmania.Goals;

import dungeonmania.Player;
import java.util.List;
import dungeonmania.Entity;

public class Or extends ComplexGoal {
    public Or(Goal left, Goal right) {
        super(left, right);
    }

    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return getLeft().hasCompleted(player, allEntities) || getRight().hasCompleted(player, allEntities);
    }

    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (this.hasCompleted(player, allEntities)) {
            return "";
        }
        return getLeft().getTypeString(player, allEntities)+" OR "+getRight().getTypeString(player, allEntities);
    }
}