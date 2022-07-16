package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashSet;
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
        if (isFriendly) return;
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
    public boolean isInInteractableRadius(Player player) {
        HashSet<Position> radiusSquare = new HashSet<>();
        radiusSquare.add(player.getPosition());
        for (int i = 0; i < DungeonManiaController.getConfigValue("bribe_radius"); i++) {
            ArrayList<Position> toBeAdded = new ArrayList<>();
            for (var square : radiusSquare) {
                toBeAdded.addAll(square.getAdjacentPositions());
            }
            radiusSquare.addAll(toBeAdded);
        }
        return radiusSquare.contains(this.getPosition());
    }

     @Override
     public void interact(Player player) throws InvalidActionException {
        if (isFriendly) throw new InvalidActionException("Already bribed");
        if (player.getInventory().stream().filter(it -> it instanceof Treasure).collect(Collectors.toList()).size() >= DungeonManiaController.getConfigValue("bribe_amount")) {
            if (isInInteractableRadius(player)) {
                for (int i = 0; i < DungeonManiaController.getConfigValue("bribe_amount"); i++) {
                    var key = player.getInventory().stream().filter(it -> it instanceof Treasure).findFirst().get().getId();
                    player.getInventory().removeIf(it -> it.getId().equals(key));
                }
                this.setFriendly(true);
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
            setInteractable(false);
            setMovementStrategy(new FriendlyMovement());
        }
    }
}