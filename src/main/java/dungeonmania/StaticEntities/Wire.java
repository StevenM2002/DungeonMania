package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import java.util.ArrayList;
import java.util.List;

public class Wire extends StaticEntity implements LogicalEntity, Switch{
    private List<LogicalEntity> observers = new ArrayList<LogicalEntity>();
    private LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    private Boolean activated = false;
    private String logicalCondition;
    private int numAdjacentActivated = 0;
    private int numAdjacentActivatedPrev = 0;

    public Wire(String id, Position position, String logicalCondition) {
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
            if (entity instanceof LogicalEntity && adjacentPositions.contains(entity.getPosition()) && !this.observers.contains((LogicalEntity) entity)) {
                this.observers.add((LogicalEntity) entity);
            }
        }
    }

    public void addBombToObserverList(List<Entity> allEntities) {
        if (logicalCondition.equals("xor")) {
            return;
        }
        List<Position> adjacentPositions = this.getPosition().getAdjacentPositions();
        for (int i = 0; i < 4; i++) {
            adjacentPositions.remove(i);
        }
        for (Entity entity : allEntities) {
            if (entity instanceof LogicalBomb && adjacentPositions.contains(entity.getPosition())) {
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
                observer.removeObserver(this);
                if (activated) {
                    observer.changeNumAdjacentActivated(1);
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
        setActivated(logicalEvaluator.evaluate(logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev));
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
        // TODO Auto-generated method stub
        return this.logicalCondition;
    }

    @Override
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }
    
}
