package dungeonmania.StaticEntities;

import java.util.List;

public class LogicalEvaluator {
    public Boolean evaluate(List<LogicalEntity> observers, String logicalCondition, int numActivated, int numActivatedPrev) {
        switch (logicalCondition) {
            case "and":
                if (numActivated >= 2) {
                    return true;
                }
                else {
                    return false;
                }
            case "or":
                if (numActivated >= 1) {
                    return true;
                }
                else {
                    return false;
                }
            case "xor":
                if (numActivated == 1) {
                    return true;
                }
                else {
                    return false;
                }
            case "co_and":
                if (numActivated >= 2 && numActivated > numActivatedPrev + 1) {
                    return true;
                }
                else {
                    return false;
                }
        }
        return null;
    }
}
