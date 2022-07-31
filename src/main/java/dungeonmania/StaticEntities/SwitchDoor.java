package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;
import dungeonmania.Entity;
import java.util.List;
import dungeonmania.response.models.EntityResponse;

public class SwitchDoor extends Door implements LogicalEntity, Switch{
    private LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    private Boolean activated = false;
    private String logicalCondition;
    private int numAdjacentActivated = 0;
    private int numAdjacentActivatedPrev = 0;

    public SwitchDoor(String id, Position position, int key, String logicalCondition) {
        super(id, position, key);
        this.logicalCondition = logicalCondition;
    }

    @Override
    public void createObserverList(List<Entity> allEntities) {}

    @Override
    public boolean getActivated() {
        return this.activated;
    }

    @Override
    public EntityResponse getEntityResponse() {
        String type;
        if (locked) {
             type = getType();
        } else {
            type = getType() + "_open";
        }
        return new EntityResponse(
                getId(),
                UtilityFunctions.camelToSnake(type),
                getPosition(),
                getIsInteractable());
    }

    @Override
    public void setActivated(boolean activated) { 
        this.activated = activated;
        if (activated) {
            super.unlock();
        }
        else {
            super.lock();
        }
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
    public void removeObserver(LogicalEntity logicalEntity) {}

    @Override
    public void registerObserver(LogicalEntity logicalEntity) {}

    @Override
    public String getLogicalCondition() {
        return this.logicalCondition;
    }

    @Override
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }

}
