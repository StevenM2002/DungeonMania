package dungeonmania.StaticEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.Player;
import dungeonmania.CollectibleEntities.Weapon;
import dungeonmania.MovingEntities.Interactable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity implements Interactable {
    private static int spawnRate;
    public ZombieToastSpawner(String id, Position position, int spawnRate) {
        super(id, position, true);
        this.spawnRate = spawnRate;
    }

    public void spawn() {

    }

    public Boolean isAdjacent(Player player) {
        int player_x = player.getPosition().getX();
        int player_y = player.getPosition().getY();
        int x = this.getPosition().getX();
        int y = this.getPosition().getY();

        if ((player_x == x - 1 && player_y == y) ||
        (player_x == x + 1 && player_y == y) ||
        (player_x == x && player_y == y - 1) ||
        (player_x == x && player_y == y + 1)) {
            return true;
        }

        return false;
    }

    @Override
    public void interact(Player player) {
        if (isAdjacent(player)) {
            if (player.getInventory().stream().anyMatch(e -> e instanceof Weapon)) {
                DungeonManiaController.getAllEntities().remove(this);
                
            } else {
                try {
                    throw new InvalidActionException("Player does not have weapon");
                } catch (InvalidActionException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                throw new InvalidActionException("Player not adjacent to ZombieSpawner");
            } catch (InvalidActionException e1) {
                e1.printStackTrace();
            }
        }
    }

}
