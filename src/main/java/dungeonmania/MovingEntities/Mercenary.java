package dungeonmania.MovingEntities;

import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.CollectibleEntities.Sceptre;

import org.json.JSONObject;

import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.PlayerListener;
import static dungeonmania.DungeonManiaController.getDmc;


public class Mercenary extends MovingEntity implements PlayerListener, Interactable, MindControl {
    private boolean isFriendly = false;
    private boolean isBribed = false;
    private int sceptreDuration = 0;
    public Mercenary(String id, Position position, double health, double attack) {
        super(id, position, true, health, attack, new FollowMovement());
    }
    public Mercenary(String id, Position position, double health, double attack, boolean isFriendly, boolean isBribed, int sceptreDuration) {
        super(id, position, true, health, attack, new FollowMovement());
        this.isBribed = isBribed;
        this.sceptreDuration = sceptreDuration;
        this.setFriendly(isFriendly);
    }
    @Override
    public void update(Potion potion) {
        if (isFriendly) return;
        if (potion == null) {
            setMovementStrategy(new FollowMovement());
        } else if (potion instanceof InvisibilityPotion) {
            setMovementStrategy(new RandomMovement());
        } else if (potion instanceof InvincibilityPotion) {
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

        if (player.getInventory().stream().anyMatch(e -> e instanceof Sceptre)) {
            Sceptre sceptre = (Sceptre) player.getInventory().stream().filter(e -> e instanceof Sceptre).findFirst().get();
            sceptreDuration = sceptre.getEffectDuration();
            player.getInventory().remove(sceptre);
        } else {
            if (!isInInteractableRadius(player)) {
                throw new InvalidActionException("Not in bribing range");
            };
            if (player.getInventory().stream().filter(it -> it instanceof Treasure).count() < getDmc().getConfigValue("bribe_amount")) {
                throw new InvalidActionException("Bribe amount is not enough");
            }
            for (int i = 0; i < getDmc().getConfigValue("bribe_amount"); i++) {
                String treasureID = player.getInventory().stream().filter(it -> it instanceof Treasure).findFirst().get().getId();
                player.getInventory().removeIf(it -> it.getId().equals(treasureID));
            }
            isBribed = true;
        }
        this.setFriendly(true);
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
        if (isFriendly) {
            setInteractable(false);
            setMovementStrategy(new FriendlyMovement());
        } else {
            setInteractable(true);
            setMovementStrategy(new FollowMovement());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("isFriendly", isFriendly);
        newJSON.put("isBribed", isBribed);
        newJSON.put("sceptreDuration", sceptreDuration);
        System.out.println("saved merc: "+newJSON.toString());
        return newJSON;
    }

    @Override
    public void updateMindControl() {
        if (sceptreDuration <= 0 && !isBribed && isFriendly) {
            setFriendly(false);
        }
        if (sceptreDuration > 0) {sceptreDuration--;}
    }
}