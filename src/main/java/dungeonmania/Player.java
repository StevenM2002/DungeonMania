package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectibleEntities.CollectibleEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements CanMove {
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
    @Override
    public void move(Direction direction) {
        collisionManager.requestMove(this, direction);
    }

    public List<CollectibleEntity> getInventory() {
        return inventory;
    }

}