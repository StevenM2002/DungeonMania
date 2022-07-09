package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Mercenary extends MovingEntity {
    public Mercenary(String id, Position position, double health, double attack) {
        super(id, position, true, health, attack);
    }
}