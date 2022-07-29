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
    private int numAdjacentActivatedPrev = 0;
    
    public LightBulb(String id, Position position, String logicalCondition) {
        super(id, position, false);
        this.logicalCondition = logicalCondition;
    }

    @Override
    public void createObserverList(List<Entity> allEntities) {
        if (logicalCondition.equals("xor")) {
            return;
        }
        List<Position> adjacentPositions = this.getPosition().getAdjacentPositions();
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
                    if (observer.evaluateLogic() != observer.getActivated()) {
                        observer.removeObserver(this);
                    }
                    observer.changeNumAdjacentActivated(1);
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
    public void setPrevNumAdjacentActivated() {
        this.numAdjacentActivatedPrev = this.numAdjacentActivated;
    }

    @Override
    public void changeNumAdjacentActivated(int change) {
        this.numAdjacentActivated += change;
        setActivated(logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev));
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
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }

}
