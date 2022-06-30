package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Position;

public class Player extends Entities {
    private List<CollectibleEntities> inventory = new ArrayList<CollectibleEntities>();
    private double attack;
    private double health;

    public Player(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    public List<CollectibleEntities> getInventory() {
        return inventory;
    }

}