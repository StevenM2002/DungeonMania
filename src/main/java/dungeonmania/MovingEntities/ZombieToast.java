package dungeonmania.MovingEntities;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements PlayerListener {
    public ZombieToast(String id, Position position, double health, double attack) {
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
}