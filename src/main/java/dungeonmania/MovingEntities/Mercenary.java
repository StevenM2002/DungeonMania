package dungeonmania.MovingEntities;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
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

    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub
        
    }
    
    public int distanceToPlayer(List<Entity> allEntities) {
        Player player = ((Player) allEntities.stream().filter(e -> e instanceof Player).collect(Collectors.toList()).get(0));
        int delta_x = Math.abs(player.getPosition().getX() - this.getPosition().getX());
        int delta_y = Math.abs(player.getPosition().getY() - this.getPosition().getY());
        return Math.max(delta_x, delta_y);
    }

    @Override
    public void interact(Player player) {

        if (player.getInventory().stream().filter(i -> i instanceof Treasure).collect(Collectors.toList()).size() == DungeonManiaController.getConfigValue("bribe_amount")) {
            if (DungeonManiaController.getConfigValue("bribe_radius") == distanceToPlayer(DungeonManiaController.getAllEntities()) &&
                !(this.getMovementStrategy() instanceof FriendlyMovement)) {

                this.setMovementStrategy(new FriendlyMovement());

            } else {
                try {
                    throw new InvalidActionException("Not in bribing range");
                } catch (InvalidActionException e) {
                    e.printStackTrace();
                }
            }

        } else {
            try {
                throw new InvalidActionException("Bribe amount is not enough");
            } catch (InvalidActionException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
        if (isFriendly) {
            setMovementStrategy(new FriendlyMovement());
        }
    }
}