package dungeonmania.MovingEntities;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.PlayerListener;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements PlayerListener {
    public ZombieToast(String id, Position position, double health, double attack) {
        super(id, position, false, health, attack, new RandomMovement());
    }

    @Override
    public void update(Potion potion) {
        if (potion == null || potion instanceof InvisibilityPotion) {
            setMovementStrategy(new RandomMovement());
        } else if (potion instanceof InvincibilityPotion) {
            setMovementStrategy(new RunningMovement());
        }
    }
}