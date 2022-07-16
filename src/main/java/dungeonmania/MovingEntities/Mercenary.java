package dungeonmania.MovingEntities;

import java.util.stream.Collectors;

import dungeonmania.DungeonManiaController;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;

public class Mercenary extends MovingEntity implements PlayerListener, Interactable {
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
        } else if (data.getPotion() instanceof InvincibilityPotion && !isFriendly) {
            setMovementStrategy(new RunningMovement());
        }
    }

    public boolean isFriendly() {
        return isFriendly;
    }
    
    public int distanceToPlayer(Player player) {
        int delta_x = Math.abs(player.getPosition().getX() - this.getPosition().getX());
        int delta_y = Math.abs(player.getPosition().getY() - this.getPosition().getY());
        return Math.max(delta_x, delta_y);
    }

    @Override
    public void interact(Player player) throws InvalidActionException {

        if (player.getInventory().stream().filter(i -> i instanceof Treasure).collect(Collectors.toList()).size() == DungeonManiaController.getConfigValue("bribe_amount")) {
            if (DungeonManiaController.getConfigValue("bribe_radius") >= distanceToPlayer(player) &&
                !(this.getMovementStrategy() instanceof FriendlyMovement)) {

                this.setMovementStrategy(new FriendlyMovement());

            } else {
                throw new InvalidActionException("Not in bribing range");
            }

        } else {
            throw new InvalidActionException("Bribe amount is not enough");
        }
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
        if (isFriendly) {
            setMovementStrategy(new FriendlyMovement());
        }
    }
}