package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class LogicalSwitch extends StaticEntity implements Switch, LogicalEntity{
    private List<LogicalEntity> observers = new ArrayList<LogicalEntity>();
    private boolean activated = false;
    private String logicalCondition;
    private LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    private int numAdjacentActivated = 0;
    private int numAdjacentActivatedPrev = 0;

    public LogicalSwitch(String id, Position position, String logicalCondition) {
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
    public boolean getActivated() {
        return activated;
    }

    @Override
    public void setActivated(boolean activated) {
        if (activated != this.activated) {
            for (LogicalEntity observer : observers) {
                if (activated) {
                    observer.removeObserver(this);
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
        return "Boulder";
    }

    @Override
    public String getType() {
        return "switch";
    }

    @Override
    public String getLogicalCondition() {
        return this.logicalCondition;
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("activated", activated);
        return newJSON;
    }

    @Override
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }
}
