package dungeonmania.MovingEntities;

import static dungeonmania.DungeonManiaController.getDmc;

public class FriendlyMovement extends Movement {
    @Override
    public void moveEntity(MovingEntity entity) {
        // Mercenary will get blocked by player in collisions if friendly
        entity.move(entity.getPosition().getDirectionTo(getDmc().getPlayer().getPreviousPosition()));
    }
}
