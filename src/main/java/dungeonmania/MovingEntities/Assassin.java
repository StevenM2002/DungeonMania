package dungeonmania.MovingEntities;

import dungeonmania.CollectibleEntities.InvincibilityPotion;
import dungeonmania.CollectibleEntities.InvisibilityPotion;
import dungeonmania.CollectibleEntities.Potion;
import dungeonmania.CollectibleEntities.Sceptre;
import dungeonmania.CollectibleEntities.Treasure;
import dungeonmania.Player;
import dungeonmania.PlayerListener;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;
import org.json.JSONObject;

import java.util.Random;

import static dungeonmania.DungeonManiaController.getDmc;

public class Assassin extends MovingEntity implements PlayerListener, Interactable, MindControl {

    private boolean isFriendly = false;
    private boolean isBribed = false;
    private int sceptreDuration = 0;

    public Assassin(String id, Position position, double health, double attack) {
        super(id, position, true, health, attack, new FollowMovement());
    }

    public Assassin(String id, Position position, double health, double attack, boolean isFriendly, boolean isBribed, int sceptreDuration) {
        super(id, position, true, health, attack, new FollowMovement());
        this.isBribed = isBribed;
        this.sceptreDuration = sceptreDuration;
        if (isFriendly) {
            this.setNowFriendly();
        }
    }

    public boolean isPlayerInRadius(Player player, int radius) {
        return Math.abs(player.getPosition().getX() - getPosition().getX()) <= radius
                && Math.abs(player.getPosition().getY() - getPosition().getY()) <= radius;
    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        if (player.getInventory().stream().anyMatch(e -> e instanceof Sceptre)) {
            Sceptre sceptre = (Sceptre) player.getInventory().stream().filter(e -> e instanceof Sceptre).findFirst().get();
            sceptreDuration = sceptre.getEffectDuration();
            player.getInventory().remove(sceptre);
        } else {
            if (isFriendly) throw new InvalidActionException("Already bribed");
            if (!isPlayerInRadius(player, getDmc().getConfigValue("bribe_radius"))) throw new InvalidActionException("Not in range to bribe");
            var amountToBribe = getDmc().getConfig().getInt("assassin_bribe_amount");
            if (player.getInventory().stream().filter(it -> it instanceof Treasure).count() < amountToBribe) throw new InvalidActionException("Not enough money");
            removeTreasureFromPlayer(amountToBribe, player);
            if (isGoingToFail()) return;
        }
        setNowFriendly();
    }
    public void setNowFriendly() {
        isFriendly = true;
        setInteractable(false);
        setMovementStrategy(new FriendlyMovement());
    }

    public boolean isFriendly() {
        return isFriendly;
    }
    private boolean isGoingToFail() {
        var rand = new Random();
        var failRate = getDmc().getConfig().getDouble("assassin_bribe_fail_rate");
        return rand.nextDouble() < failRate;
    }
    private void removeTreasureFromPlayer(int amountToRemove, Player player) {
        for (int i = 0; i < amountToRemove; i++) {
            var key = player.getInventory().stream().filter(it -> it instanceof Treasure).findFirst().get().getId();
            player.getInventory().removeIf(it -> it.getId().equals(key));
        }
    }

    @Override
    public void update(Potion potion) {
        if (isFriendly) return;
        if (potion == null) {
            setMovementStrategy(new FollowMovement());
        } else if (potion instanceof InvincibilityPotion) {
            setMovementStrategy(new RunningMovement());
        } else if (potion instanceof InvisibilityPotion) {
            if (isPlayerInRadius(getDmc().getPlayer(), getDmc().getConfig().getInt("assassin_recon_radius"))) {
                if (!(getMovementStrategy() instanceof FollowMovement)) setMovementStrategy(new FollowMovement());
            } else {
                setMovementStrategy(new RandomMovement());
            }
        }
    }
    @Override
    public JSONObject toJSON() {
        JSONObject newJSON = super.toJSON();
        newJSON.put("isFriendly", isFriendly);
        newJSON.put("isBribed", isBribed);
        newJSON.put("sceptreDuration", sceptreDuration);
        return newJSON;
    }

    @Override
    public void updateMindControl() {
        if (sceptreDuration <= 0 && !isBribed) {
            isFriendly = false;
            setInteractable(true);
        }
        sceptreDuration--;
    }
}
