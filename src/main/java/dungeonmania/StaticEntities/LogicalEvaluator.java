package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import java.util.List;

public class LogicalEvaluator {
    public Boolean evaluate(List<LogicalEntity> observers, String logicalCondition, int numActivated) {
        switch (logicalCondition) {
            case "AND":
                if (numActivated >= 2) {
                    return true;
                }
                else {
                    return false;
                }
            case "OR":
                if (numActivated >= 1) {
                    return true;
                }
                else {
                    return false;
                }
            case "XOR":
                if (numActivated == 1) {
                    return true;
                }
                else {
                    return false;
                }
            case "CO_AND":
            // shit
        }
        return null;
    }
}
