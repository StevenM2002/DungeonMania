package dungeonmania;

import dungeonmania.util.Position;

public class Spider extends MovingEntities {
    private double health;
    private double attack;

    public Spider(String id, Position position, boolean isInteractable, Movement movingStrategy) {
        super(id, position, isInteractable, new Follow());
    }

}
