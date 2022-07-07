package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entities {
    private List<CollectibleEntities> inventory = new ArrayList<>();
    private double attack;
    private double health;

    public Player(String id, Position position) {
        super(id, position, true);
    }

    public void move(List<Position> blockingEntities, Direction direction) {
        Position nextPosition = getPosition().translateBy(direction);
        if (blockingEntities.stream().anyMatch(position -> position.equals(nextPosition))) {
            setPosition(nextPosition);
        }
    }

    public List<CollectibleEntities> getInventory() {
        return inventory;
    }

}