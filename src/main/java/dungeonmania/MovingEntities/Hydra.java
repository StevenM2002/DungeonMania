package dungeonmania.MovingEntities;

import java.util.Random;

import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements PlayerListener {
    private static double increaseRate;
    private static double increaseAmount;

    public Hydra(String id, Position position, double health, double attack, double healthIncreaseRate, double healthIncreaseAmount) {
        super(id, position, false, health, attack, new RandomMovement());
    }

    @Override
    public void update(PlayerDataArgs data) {
        if (data.getPotion() == null || data.getPotion() instanceof InvisibilityPotion) {
            setMovementStrategy(new RandomMovement());
        } else if (data.getPotion() instanceof InvincibilityPotion) {
            setMovementStrategy(new RunningMovement());
        }
    }

    @Override
    public double takeDamage(double damage) {
        Random rand = new Random();
        if (rand.nextDouble() <= increaseRate) {
            setHealth(getHealth() + increaseAmount);
            return -increaseAmount;
        }
        return super.takeDamage(damage);
    }
}
