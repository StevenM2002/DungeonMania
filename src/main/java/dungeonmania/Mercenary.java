package dungeonmania;

import java.util.List;

import dungeonmania.util.Position;

public class Mercenary extends MovingEntities {
    private double health;
    private double attack;
    private boolean isEnemy = true;

    public Mercenary(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    public void bribe(List<CollectibleEntities> inventory) {

    }
}