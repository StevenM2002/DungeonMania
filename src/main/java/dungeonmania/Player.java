package dungeonmania;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
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
=======
import dungeonmania.CollectibleEntities.CollectibleEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity {
    private List<CollectibleEntity> inventory = new ArrayList<>();
    private double attack;
    private double health;

    public Player(String id, Position position, double health, double attack) {
        super(id, position, false);
        this.health = health;
        this.attack = attack;
    }

    /**
     * If there is no blocking entity in the next move, then move the player one block down else do not move
     * @param blockingEntities the positions of any entities which can block the movement of the player into that position
     * @param direction the direction of movement
     */
    public void move(Direction direction) {
        setPosition(getPosition().translateBy(direction));
    }

    public List<CollectibleEntity> getInventory() {
>>>>>>> 598efdb7244af17b56af44b51c5d66adf058c04c
        return inventory;
    }

}