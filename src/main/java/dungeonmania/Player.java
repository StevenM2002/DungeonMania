package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.CollectibleEntities.Shield;
import dungeonmania.CollectibleEntities.Sword;
import dungeonmania.MovingEntities.Battling;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.CollectibleEntities.Bow;
import dungeonmania.CollectibleEntities.InventoryObject;
import dungeonmania.CollectibleEntities.MidnightArmor;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements CanMove, Battling {
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

    public Potion getCurrentEffect() {
        return potionManager.getCurrPotion();
    }
    
    public List<InventoryObject> getInventory() {
        return inventory;
    }

    @Override
    public String getDefaultCollision() {
        return "Battle";
    }

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
            if (midnightArmor.deteriorate()) {
                inventory.remove(midnightArmor);
            }
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
    public double takeDamage(Entity entity) {
        return (((MovingEntity) entity).getAttack() - getShieldMod() - getMidnightArmorDefence()) / 10;
    }
    @Override
    public double dealDamage(Entity entity) {
        return (getBowMod() * (this.getAttack() + getSwordMod()) + getMidnightArmorAttack()) / 5;
    }
}
