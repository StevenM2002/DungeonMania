package dungeonmania.Goals;

import dungeonmania.Player;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import java.util.List;

import dungeonmania.BattleManager;
import dungeonmania.Entity;

public class EnemyGoal implements Goal{
    private int requiredEnemies;
    private BattleManager battleManager;
    public EnemyGoal(int requiredEnemies, BattleManager battleManager) {
        this.requiredEnemies = requiredEnemies;
        this.battleManager = battleManager;
    }
    @Override
    public Boolean hasCompleted(Player player, List<Entity> allEntities) {
        // Find out how many spawners there are
        boolean hasSpawner = false;
        for (Entity entity : allEntities) {
            if (entity instanceof ZombieToastSpawner) {
                hasSpawner = true;
                break;
            }
        }
        if (!hasSpawner && battleManager.getVictimCount() >= requiredEnemies) {
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public String getTypeString(Player player, List<Entity> allEntities) {
        if (hasCompleted(player, allEntities)) {
            return "";
        }
        return ":enemies";
    }
}