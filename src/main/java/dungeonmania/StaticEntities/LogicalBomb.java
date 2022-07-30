package dungeonmania.StaticEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.util.Position;
import dungeonmania.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;
import dungeonmania.Player;
import dungeonmania.response.models.EntityResponse;

public class LogicalBomb extends ActiveBomb implements LogicalEntity, Switch{
    private List<LogicalEntity> observers = new ArrayList<LogicalEntity>();
    private Boolean activated = false;
    private LogicalEvaluator logicalEvaluator = new LogicalEvaluator();
    private String logicalCondition;
    private int numAdjacentActivated = 0;
    private int numAdjacentActivatedPrev = 0;
    private int radius = DungeonManiaController.getDmc().getConfig().getInt("bomb_radius");

    public LogicalBomb(String id, Position position, String logicalCondition) {
        super(id, position, false);
        this.logicalCondition = logicalCondition;
    }

    @Override
    public boolean isGoingToExplode(List<Entity> allEntities) {
        return getActivated();
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
    public boolean getActivated() {
        return this.activated;
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
        // TODO Auto-generated method stub
        return this.logicalCondition;
    }

    @Override
    public boolean evaluateLogic() {
        return logicalEvaluator.evaluate(observers, logicalCondition, numAdjacentActivated, numAdjacentActivatedPrev);
    }

    private List<Position> getPositionsInDetonationRadius() {
        HashSet<Position> detonationPositions = new HashSet<>();
        // If radius is equal to 0 then explode self and nothing else
        detonationPositions.add(this.getPosition());
        for (int i = 0; i < radius; i++) {
            List<Position> positions = new ArrayList<>(detonationPositions);
            for (var position : positions) {
                detonationPositions.addAll(position.getAdjacentPositions());
            }
        }
        return new ArrayList<>(detonationPositions);
    }

    public List<Entity> getEntitiesInRadiusIfExplode(List<Entity> allEntities) {
        return allEntities
                .stream()
                .filter(entity -> !(entity instanceof Player) && getPositionsInDetonationRadius().contains(entity.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(
                this.getId(),
                "bomb",
                this.getPosition(),
                this.getIsInteractable());
    }
    @Override
    public String getDefaultCollision() {
        return "Block";
    }
}
