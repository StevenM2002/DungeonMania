package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    public ZombieToast(String id, Position position, double health, double attack) {
        super(id, position, false, health, attack, new RandomMovement());
    }
}