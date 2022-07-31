package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import java.util.List;

public class LightBulb extends StaticEntity implements LogicalEntity, Switch{
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
        // I know having functions that do nothing is bad, but I wrote this thinking all logical entities carry current and no I really can't be stuffed
        // to rearrange all my code so that lighbulbs etc. don't have to override the observer list functions so this is what I'm doing. Surely cut me some
        // slack since a forum tutor only told me about lightbulbs etc. not conducting current a few hours before the due time
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
    public void registerObserver(LogicalEntity logicalEntity) {}

    @Override
    public void removeObserver(LogicalEntity logicalEntity) {}

    @Override
    public String getLogicalCondition() {
        return this.logicalCondition;
    }
    
    @Override
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }

}
