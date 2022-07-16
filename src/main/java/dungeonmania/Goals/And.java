package dungeonmania.Goals;

import dungeonmania.Player;
import java.util.List;
import dungeonmania.Entity;

public class And extends ComplexGoal {
    public And(Goal left, Goal right) {
        super(left, right);
    }

    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return getLeft().hasCompleted(player, allEntities) && getRight().hasCompleted(player, allEntities);
    }

    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (getLeft().hasCompleted(player, allEntities)) {
            return getRight().getTypeString(player, allEntities);
        } else if (getRight().hasCompleted(player, allEntities)) {
            return getLeft().getTypeString(player, allEntities);
        }
        return (getLeft().getTypeString(player, allEntities)+" AND "+getRight().getTypeString(player, allEntities));
    }
}
