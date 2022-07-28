package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.CollectibleEntities.*;
import dungeonmania.Collisions.CollisionManager;
import dungeonmania.MovingEntities.Battling;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements CanMove, Battling {
    private Position previousPosition = getPosition();
    private List<InventoryObject> inventory = new ArrayList<>();
    private double attack;
    private double health;
    private List<PlayerListener> subscribers = new ArrayList<>();

    
    public Player(String id, Position position, double health, double attack) {
        super(id, position, false);
        this.health = health;
        this.attack = attack;
    }
    public Player(String id, Position position, Position previousPosition,
            JSONArray inventory, double attack, double health) {
        super(id, position, false);
        this.previousPosition = previousPosition;
        this.attack = attack;
        this.health = health;
        for (int i = 0; i < inventory.length(); i++) {
            JSONObject item = inventory.getJSONObject(i);
            this.inventory.add(ItemFactory.createItem(item.getString("type"), item.getString("id"), item));
        }
        
    }



    public void removeInventoryItem(InventoryObject item) {
        inventory.remove(item);
    }

    public Position getPreviousPosition() {
        return previousPosition;
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
     * Moves the player, and records its previous position.
     * @param direction the direction of movement
     */
    @Override
    public void move(Direction direction) {
        var tempPosition = getPosition();
        CollisionManager.requestMove(this, direction);
        this.previousPosition = tempPosition;
    }

    
    public List<InventoryObject> getInventory() {
        return inventory;
    }

    @Override
    public String getDefaultCollision() {
        return "Battle";
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("health", health);
        newJSON.put("prevX", previousPosition.getX());
        newJSON.put("prevY", previousPosition.getY());
        newJSON.put("inventory", new JSONArray());
        for (InventoryObject i : inventory) {
            newJSON.getJSONArray("inventory").put(i.toJSON());
        }
        return newJSON;
    }

    ////////////////////////////////////////////////////////////////////////////
    //                            Potion Logic                                //
    ////////////////////////////////////////////////////////////////////////////

    
    // String is the event which the listener wants to subscribe to
    // This includes:
    // playerMovement, playerPotionEffect
    public void subscribe(PlayerListener subscriber) {
        subscribers.add(subscriber);
    }


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
        PotionManager.addPotionEffect(potion.getDurationEffect(), potion);
        inventory.remove(inventoryObject);
    }
    public void doPotionTick() {
        Potion potion = PotionManager.getNextEffect();
        notify(potion);
    }
    public void notify(Potion potion) {
        subscribers.forEach(publisherListener -> publisherListener.update(potion));
    }

    public Potion getCurrentEffect() {
        return PotionManager.getCurrPotion();
    }



    ////////////////////////////////////////////////////////////////////////////
    //                             Batle Logic                                //
    ////////////////////////////////////////////////////////////////////////////

    private Bow getBow() {
        if (this.inventory.stream().anyMatch(e -> e instanceof Bow)) {
            return ((Bow) this.inventory.stream()
                    .filter(e -> e instanceof Bow)
                    .findFirst().get());
        }

        return null;
    }

    private Sword getSword() {
        if (this.inventory.stream().anyMatch(e -> e instanceof Sword)) {
            return ((Sword) this.inventory.stream()
                    .filter(e -> e instanceof Sword)
                    .findFirst().get());
        }

        return null;
    }

    private Shield getShield() {
        if (this.inventory.stream().anyMatch(e -> e instanceof Shield)) {
            return ((Shield) this.inventory.stream()
                    .filter(e -> e instanceof Shield)
                    .findFirst().get());
        }

        return null;
    }

    private MidnightArmor getMidnightArmor() {
        if (this.inventory.stream().anyMatch(e -> e instanceof MidnightArmor)) {
            return ((MidnightArmor) this.inventory.stream()
                    .filter(e -> e instanceof MidnightArmor)
                    .findFirst().get());
        }

        return null;
    }


    private double getBowMod() {
        Bow bow = getBow();

        if (bow != null) {
            if (bow.deteriorate()) {
                this.inventory.remove(bow);
            }

            return bow.getModifier();
        }

        return 1;
    }

    private double getSwordMod() {
        Sword sword = getSword();

        if (sword != null) {
            if (sword.deteriorate()) {
                inventory.remove(sword);
            }
            return sword.getModifier();
        }

        return 0;
    }

    private double getShieldMod() {
        Shield shield = getShield();

        if (shield != null) {
            if (shield.deteriorate()) {
                inventory.remove(shield);
            }
            return shield.getDefence();
        }
        return 0;
    }

    private double getMidnightArmorAttack() {
        MidnightArmor midnightArmor = getMidnightArmor();

        if (midnightArmor != null) {
            return midnightArmor.getModifier();
        }
        return 0;
    }

    private double getMidnightArmorDefence() {
        MidnightArmor midnightArmor = getMidnightArmor();

        if (midnightArmor != null) {
            return midnightArmor.getDefence();
        }
        return 0;
    }

    @Override
    public double takeDamage(double damage) {
        if (PotionManager.getCurrPotion() instanceof InvincibilityPotion) return 0;
        double damageTaken = (damage  - getShieldMod() - getMidnightArmorDefence()) / 10;
        if (health - damageTaken < 0) {
            damageTaken = health;
        }
        health -= damageTaken;
        return damageTaken;
    }
    @Override
    public double dealDamage() {
        return (getBowMod() * (getAttack() + getSwordMod()) + getMidnightArmorAttack());
    }
}
