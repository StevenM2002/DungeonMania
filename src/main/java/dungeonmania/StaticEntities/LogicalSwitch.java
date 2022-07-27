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

    public LogicalSwitch(String id, Position position, String logicalCondition, List<Entity> allEntities) {
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
    public boolean getActivated() {
        return activated;
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
    public void evaluateLogic() {
        if (logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated)) {
            setActivated(true);
        }
        else {
            setActivated(false);
        };
    }
}
