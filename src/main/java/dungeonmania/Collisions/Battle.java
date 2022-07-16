package dungeonmania.Collisions;

import dungeonmania.BattleManager;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Direction;

public class Battle extends Collision {
    private BattleManager battleManager;

    public Battle(BattleManager battleManager) {
        this.battleManager = battleManager;
    }


    @Override
    public void processCollision(Entity moved, Entity collided, Direction direction) {
        Player player;
        MovingEntity enemy;
        if (moved instanceof Player) {
            player = (Player) moved;
            enemy = (MovingEntity) collided;
        } else {
            player = (Player) collided;
            enemy = (MovingEntity) moved;
        }
        moved.setPosition(collided.getPosition());
        battleManager.doBattle(player, enemy);
    }
}
