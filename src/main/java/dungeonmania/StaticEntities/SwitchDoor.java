package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import java.util.ArrayList;
import java.util.List;

public class SwitchDoor extends Door implements LogicalEntity, Switch{
    private List<LogicalEntity> observers = new ArrayList<LogicalEntity>();
    LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    Boolean activated = false;
    String logicalCondition;
    private int numAdjacentActivated = 0;

    public SwitchDoor(String id, Position position, int key, String logicalCondition, List<Entity> allEntities) {
        super(id, position, key);
        this.logicalCondition = logicalCondition;
        List<Position> adjacentPositions = position.getAdjacentPositions();
        for (int i = 0; i < 4; i++) {
            adjacentPositions.remove(i);
        }
        for (Entity entity : allEntities) {
            if (entity instanceof LogicalEntity && adjacentPositions.contains(entity.getPosition())) {
                this.observers.add((LogicalEntity) entity);
            }
        }
    }

    @Override
    public boolean getActivated() {
        return this.activated;
    }

    @Override
    public void setActivated(boolean activated) {
        if (activated != this.activated) {
            for (LogicalEntity observer : observers) {
                if (activated) {
                    observer.changeNumAdjacentActivated(1);
                    observer.removeObserver(this);
                    if (!this.observers.contains(observer)) {
                        this.observers.add(observer);
                    }
                }
                else {
                    observer.changeNumAdjacentActivated(-1);
                }
            }
        }   
        this.activated = activated;
    }

    @Override
    public String getActivationType() {
        return "Switch";
    }
    
    @Override
    public void changeNumAdjacentActivated(int change) {
        this.numAdjacentActivated += change;
        evaluateLogic();
    }

    @Override
    public void removeObserver(LogicalEntity logicalEntity) {
        observers.remove(logicalEntity);
    }

    @Override
    public void registerObserver(LogicalEntity logicalEntity) {
        observers.add(logicalEntity);
    }

    @Override
    public String getLogicalCondition() {
        return this.logicalCondition;
    }

    @Override
    public void evaluateLogic() {
        if (logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated)) {
            setActivated(true);
        }
        else {
            setActivated(false);
        };
    }

}
