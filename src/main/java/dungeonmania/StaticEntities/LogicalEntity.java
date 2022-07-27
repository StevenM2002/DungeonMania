package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import java.util.List;

public interface LogicalEntity {
    public void removeObserver(LogicalEntity logicalEntity);
    public void registerObserver(LogicalEntity logicalEntity);
    public void createObserverList(List<Entity> allEntities);
    public void changeNumAdjacentActivated(int change);
    public String getLogicalCondition();
    public void evaluateLogic();
}
