package dungeonmania.MovingEntities;

import dungeonmania.Player;

public abstract class Movement {
    public static Player player;
    public abstract void moveEntity(MovingEntity entity);
}
