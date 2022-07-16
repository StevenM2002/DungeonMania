package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.CollectibleEntities.Potion;
import org.json.JSONObject;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements CanMove {
    private Position previousPosition = getPosition();
    public Position getPreviousPosition() {
        return previousPosition;
    }
    private List<InventoryObject> inventory = new ArrayList<>();
    private double attack;
    private double health;
    private List<PlayerListener> subscribers = new ArrayList<>();
    // String is the event which the listener wants to subscribe to
    // This includes:
    // playerMovement, playerPotionEffect
    public void subscribe(PlayerListener subscriber) {
        subscribers.add(subscriber);
    }
    public void unsubscribe(String eventType, PlayerListener subscriber) {
        subscribers.remove(subscriber);
    }
    private PotionManager potionManager = new PotionManager();

    /**
     * Assuming id given is to a potion
     * @param id of a potion
     */
    public void queuePotion(String id) {
        InventoryObject inventoryObject = inventory.stream().filter(it -> it.getId().equals(id)).findFirst().get();
        if (!(inventoryObject instanceof Potion)) {
            return;
        }
        Potion potion = (Potion) inventoryObject;
        potionManager.addPotionEffect(potion.getDurationEffect(), potion);
        inventory.remove(inventoryObject);
    }
    public void doPotionTick() {
        Potion potion = potionManager.getNextEffect();
        PlayerDataArgs data = new PlayerDataArgs();
        data.setPotion(potion);
        notify(data);
    }
    public void notify(PlayerDataArgs data) {
        subscribers.forEach(publisherListener -> publisherListener.update(data));
    }
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
     * @param direction the direction of movement
     */
    @Override
    public void move(Direction direction) {
        var tempPosition = getPosition();
        collisionManager.requestMove(this, direction);
        this.previousPosition = tempPosition;
    }

    public List<InventoryObject> getInventory() {
        return inventory;
    }

    @Override
    public String getDefaultCollision() {
        return "Battle";
    }
    
    //TODO: move this out of player
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
