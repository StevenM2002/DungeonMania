package dungeonmania.MovingEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements PlayerListener{
    private double increaseRate = DungeonManiaController.getConfigValue("hydra_health_increase_rate");

    public Hydra(String id, Position position, double health, double attack) {
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
