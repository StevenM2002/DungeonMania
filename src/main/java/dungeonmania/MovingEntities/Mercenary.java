package dungeonmania.MovingEntities;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements PlayerListener {
    private boolean isFriendly = false;
    public Mercenary(String id, Position position, double health, double attack) {
        super(id, position, true, health, attack, new FollowMovement());
    }
    @Override
    public void update(PlayerDataArgs data) {
        if (data.getPotion() == null && isFriendly) {
            setMovementStrategy(new FriendlyMovement());
        } else if (data.getPotion() == null && !isFriendly) {
            setMovementStrategy(new FollowMovement());
        } else if (data.getPotion() instanceof InvisibilityPotion) {
            setMovementStrategy(new RandomMovement());
        } else if (data.getPotion() instanceof InvincibilityPotion) {
            setMovementStrategy(new RunningMovement());
        }
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
    }
}