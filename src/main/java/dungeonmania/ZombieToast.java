package dungeonmania;

import dungeonmania.util.Position;

public class ZombieToast extends MovingEntities {
    private double health;
    private double attack;

    public ZombieToast(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }
}