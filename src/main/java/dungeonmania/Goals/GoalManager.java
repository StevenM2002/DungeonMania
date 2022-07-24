package dungeonmania.Goals;
import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.BattleManager;

public class GoalManager {
    /**
     * rercursively loads the goals
     * @param goal_condition
     * @param config
     * @return
     */
    public static Goal loadGoals(JSONObject dungeon, JSONObject config, BattleManager battleManager) { // Recursion
        // Leaf goals (Base cases)
        JSONObject goal_condition = dungeon.optJSONObject("goal-condition");
        if (goal_condition.length() == 1) {
            switch (goal_condition.getString("goal")) {
                case "exit":
                    return new ExitGoal();
                case "treasure":
                    return new TreasureGoal(config.getInt("treasure_goal"));
                case "enemies":
                    return new EnemyGoal(config.getInt("enemy_goal"), battleManager);
                case "boulders":
                    return new SwitchesGoal();
            }
        } else if (goal_condition.length() == 0) {
            return new EmptyGoal();
        }
        // Complex goals
        else {
            JSONArray subgoals = goal_condition.getJSONArray("subgoals");
            JSONObject left = subgoals.getJSONObject(0);
            JSONObject right = subgoals.getJSONObject(1);           
            switch (goal_condition.getString("goal")) {
                case "AND":             
                    return new And(loadGoals(left, config, battleManager), loadGoals(right, config, battleManager));
                case "OR":
                    return new Or(loadGoals(left, config, battleManager), loadGoals(right, config, battleManager));
            }
        }
        return new EmptyGoal();
    }
}
