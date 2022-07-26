package dungeonmania.Goals;

import dungeonmania.Player;
import dungeonmania.CollectibleEntities.*;
import java.util.List;
import dungeonmania.Entity;

public class TreasureGoal implements Goal{
    private int requiredTreasure;
    public TreasureGoal (int requiredTreasure) {
        this.requiredTreasure = requiredTreasure;
    }
    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        return player.getInventory().stream()
            .filter(x->(x instanceof Treasure || x instanceof Sunstone))
            .count() >= requiredTreasure;
    }
    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (hasCompleted(player, allEntities)) {
            return "";
        }
        return ":treasure";
    }
}
