package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    public Spider(String id, Position position, double health, double attack) {
        super(id, position, false, health, attack, new CircularMovement());
    }
}
