package dungeonmania.StaticEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.UtilityFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveBomb extends StaticEntity {

    private int radius = DungeonManiaController.getDmc().getConfig().getInt("bomb_radius");

    public ActiveBomb(String id, Position position, boolean isInteractable) {
        super(id, position, isInteractable);
    }

    private List<Position> getPositionsInDetonationRadius() {
        HashSet<Position> detonationPositions = new HashSet<>();
        // If radius is equal to 0 then explode self and nothing else
        detonationPositions.add(this.getPosition());
        for (int i = 0; i < radius; i++) {
            List<Position> positions = new ArrayList<>(detonationPositions);
            for (var position : positions) {
                detonationPositions.addAll(position.getAdjacentPositions());
            }
        }
        return new ArrayList<>(detonationPositions);
    }

    public boolean isGoingToExplode(List<Entity> allEntities) {
        List<Direction> directions = Arrays.asList(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN);
        List<Position> cardinalPositions = new ArrayList<>();
        directions.forEach(direction -> cardinalPositions.add(this.getPosition().translateBy(direction)));
        List<Entity> entitiesInCardinalPosition = allEntities.stream().filter(entity -> cardinalPositions.contains(entity.getPosition())).collect(Collectors.toList());
        return entitiesInCardinalPosition.stream().anyMatch(entity -> entity instanceof FloorSwitch && ((FloorSwitch) entity).getActivated());
    }

    public List<Entity> getEntitiesInRadiusIfExplode(List<Entity> allEntities) {
        return allEntities
                .stream()
                .filter(entity -> !(entity instanceof Player) && getPositionsInDetonationRadius().contains(entity.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(
                this.getId(),
                "bomb",
                this.getPosition(),
                this.getIsInteractable());
    }
    @Override
    public String getDefaultCollision() {
        return "Block";
    }
}
