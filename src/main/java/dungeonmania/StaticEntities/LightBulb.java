package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import java.util.ArrayList;
import java.util.List;

public class LightBulb extends StaticEntity implements LogicalEntity, Switch{
    private List<LogicalEntity> observers = new ArrayList<LogicalEntity>();
    private Boolean activated = false;
    private LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    private String logicalCondition;
    private int numAdjacentActivated = 0;
    
    public LightBulb(String id, Position position, String logicalCondition, List<Entity> allEntities) {
        super(id, position, false);
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

    public String getTypeString() {
        if (this.activated) {
            return "light_bulb_on";
        }
        else {
            return "light_bulb_off";
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
    public void registerObserver(LogicalEntity logicalEntity) {
        observers.add(logicalEntity);
    }

    @Override
    public void removeObserver(LogicalEntity logicalEntity) {
        observers.remove(logicalEntity);
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
