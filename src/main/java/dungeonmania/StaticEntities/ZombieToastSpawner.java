package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Random;

import dungeonmania.*;
import static dungeonmania.DungeonManiaController.getDmc;


import dungeonmania.CollectibleEntities.Weapon;
import dungeonmania.MovingEntities.Interactable;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity implements Interactable {
    private static int spawnRate;
    private Random random;

    public ZombieToastSpawner(String id, Position position, int spawnRate) {
        super(id, position, true);
        ZombieToastSpawner.spawnRate = spawnRate;
        random = new Random();
    }


    public void spawn(int currTick) {
        if (spawnRate == 0 || currTick % spawnRate != 0) return;

        ZombieToast newZombie = (ZombieToast) EntityFactory.createEntity("zombie_toast", getPosition(), null);
        List<Direction> directionsToMove = Direction.allDirections();
        newZombie.move(directionsToMove.get(random.nextInt(3)));
        for (int i = 0; i < 4 && newZombie.getPosition().equals(getPosition()); i++) {
            newZombie.move(directionsToMove.get(i));
        }
        if (newZombie.getPosition().equals(getPosition())) {
            getDmc().removeFromEntities(newZombie);;
        }

    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        if (Position.isAdjacent(player.getPosition(), getPosition())) {
            if (player.getInventory().stream().anyMatch(e -> e instanceof Weapon)) {
                getDmc().removeFromEntities(this);

            } else {
                throw new InvalidActionException("Player does not have weapon");
            }

        } else {
            throw new InvalidActionException("Player not adjacent to ZombieSpawner");
        }
    }
}
