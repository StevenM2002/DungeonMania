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

    @Override
    public void interact(Player player) {
        // if (Position.isAdjacent(player.getPosition(), getPosition())) {
        //     if (player.getInventory().stream().anyMatch(e -> e instanceof Weapon)) {
        //         DungeonManiaController.getAllEntities().remove(this);
                
        //     } else {
        //         try {
        //             throw new InvalidActionException("Player does not have weapon");
        //         } catch (InvalidActionException e1) {
        //             e1.printStackTrace();
        //         }
        //     }
        // } else {
        //     try {
        //         throw new InvalidActionException("Player not adjacent to ZombieSpawner");
        //     } catch (InvalidActionException e1) {
        //         e1.printStackTrace();
        //     }
        // }
    }

}
