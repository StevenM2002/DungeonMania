package dungeonmania.StaticEntities;

public interface LogicalEntity {
    public void removeObserver(LogicalEntity logicalEntity);
    public void registerObserver(LogicalEntity logicalEntity);
    public void changeNumAdjacentActivated(int change);
    public String getLogicalCondition();
    public void evaluateLogic();
}
