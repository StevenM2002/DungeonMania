package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.*;
import org.json.JSONObject;

import dungeonmania.CollectibleEntities.Weapon;
import dungeonmania.MovingEntities.Interactable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity implements Interactable {
    private static int spawnRate;
    private static int id_counter = 0;

    public ZombieToastSpawner(String id, Position position, int spawnRate) {
        super(id, position, true);
        ZombieToastSpawner.spawnRate = spawnRate;
    }

    private Position findNewPosition(List<Entity> allEntities) {
        int x = this.getPosition().getX();
        int y = this.getPosition().getY();
        if (!allEntities.stream().anyMatch(entity -> entity.getPosition().equals(new Position(x + 1, y)))) {
            return new Position(x + 1, y);

        } else if (!allEntities.stream().anyMatch(entity -> entity.getPosition().equals(new Position(x - 1, y)))) {
            return new Position(x - 1, y);

        } else if (!allEntities.stream().anyMatch(entity -> entity.getPosition().equals(new Position(x, y + 1)))) {
            return new Position(x, y + 1);

        } else if (!allEntities.stream().anyMatch(entity -> entity.getPosition().equals(new Position(x - 1, y)))) {
            return new Position(x, y - 1);
        }

        return null;
    }

    public void spawn(List<Entity> allEntities, int currTick) {
        if (spawnRate == 0 || currTick % spawnRate != 0) return;

        Position newPosition = findNewPosition(allEntities);
        String zombie_id = this.getId() + "zombie" + id_counter;
        id_counter++;
        if (newPosition != null) {
            JSONObject config = DungeonManiaController.getConfig();
            JSONObject jsonEntity = new JSONObject();
            jsonEntity.put("type", "zombie_toast");
            jsonEntity.put("x", newPosition.getX());
            jsonEntity.put("y", newPosition.getY());
            Entity zombieToast = EntityFactory.createEntity(zombie_id, jsonEntity, config);
            Player player = (Player) allEntities.stream().filter(entity -> entity instanceof Player).findFirst().get();
            player.subscribe((PlayerListener) zombieToast);
            allEntities.add(zombieToast);
        }

    }

    @Override
    public void interact(Player player) throws InvalidActionException {
        if (Position.isAdjacent(player.getPosition(), getPosition())) {
            if (player.getInventory().stream().anyMatch(e -> e instanceof Weapon)) {
                DungeonManiaController.removeFromEntities(this);

            } else {
                throw new InvalidActionException("Player does not have weapon");
            }

        } else {
            throw new InvalidActionException("Player not adjacent to ZombieSpawner");
        }
    }
}
