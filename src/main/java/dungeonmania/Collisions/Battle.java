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
        Player player = (Player) moved;
        MovingEntity enemy = (MovingEntity) collided;
        player.setPosition(enemy.getPosition());
        battleManager.doBattle(player, enemy);
    }
}
