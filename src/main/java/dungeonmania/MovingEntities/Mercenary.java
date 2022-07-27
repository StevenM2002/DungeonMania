package dungeonmania.MovingEntities;

import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.PlayerDataArgs;
import dungeonmania.PlayerListener;
import static dungeonmania.DungeonManiaController.getDmc;


public class Mercenary extends MovingEntity implements PlayerListener, Interactable {
    private boolean isFriendly = false;
    public Mercenary(String id, Position position, double health, double attack) {
        super(id, position, true, health, attack, new FollowMovement());
    }


    @Override
    public void update(PlayerDataArgs data) {
        if (isFriendly) return;
        if (data.getPotion() == null) {
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

    /**
     * returns true if the player is within bribing distance of the mercenary
     * @param player
     * @return
     */
    public boolean isInInteractableRadius(Player player) {
        return Math.abs(player.getPosition().getX() - getPosition().getX()) <= getDmc().getConfigValue("bribe_radius") 
            && Math.abs(player.getPosition().getY() - getPosition().getY()) <= getDmc().getConfigValue("bribe_radius");
    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        if (isFriendly) throw new InvalidActionException("Already bribed");
        if (!isInInteractableRadius(player)) {
            throw new InvalidActionException("Not in bribing range");
        };
        if (player.getInventory().stream().filter(it -> it instanceof Treasure).collect(Collectors.toList()).size() < getDmc().getConfigValue("bribe_amount")) {
            throw new InvalidActionException("Bribe amount is not enough");
        }
        for (int i = 0; i < getDmc().getConfigValue("bribe_amount"); i++) {
            var key = player.getInventory().stream().filter(it -> it instanceof Treasure).findFirst().get().getId();
            player.getInventory().removeIf(it -> it.getId().equals(key));
        }
        this.setFriendly(true);
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
        if (isFriendly) {
            setInteractable(false);
            setMovementStrategy(new FriendlyMovement());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("isFriendly", isFriendly);
        return newJSON;
    }
}