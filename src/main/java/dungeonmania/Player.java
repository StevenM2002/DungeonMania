package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements CanMove {
    private List<InventoryObject> inventory = new ArrayList<>();
    private double attack;
    private double health;

    public Player(String id, Position position, double health, double attack) {
        super(id, position, false);
        this.health = health;
        this.attack = attack;
    }

    public double getAttack() {
        return attack;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
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

    public List<InventoryObject> getInventory() {
        return inventory;
    }
    
    public void addCraftItemToInventory(String Item, JSONObject config, int noOfEntities) throws IllegalArgumentException, InvalidActionException {
        int shieldDurability = config.getInt("shield_durability");
        int bowDurability = config.getInt("bow_durability");
        int defence = config.getInt("shield_defence");
        switch (Item) {
            case "bow":
                Bow newBow = new Bow(String.valueOf(noOfEntities), 2, bowDurability);
                newBow.craft(inventory);
            case "shield":
                Shield newShield = new Shield(String.valueOf(noOfEntities), defence, shieldDurability);
                newShield.craft(inventory);
        }
    }
}
