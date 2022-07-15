package dungeonmania.MovingEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CircularMovement implements Movement {
    private int currentRotation = 0;
    private boolean isReversed = false;

    @Override
    public Position getNextPosition(Player player, Position currentPosition) {
        List<Position> movementTrajectory = new ArrayList<Position>();
        List<Position> reverseTrajectory = new ArrayList<Position>();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        Position newPosition = new Position(x, y);

        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x, y + 1));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x - 1, y));
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x, y - 1));
        movementTrajectory.add(new Position(x + 1, y));

        reverseTrajectory.add(new Position(x, y + 1));
        reverseTrajectory.add(new Position(x - 1, y));
        reverseTrajectory.add(new Position(x - 1, y));
        reverseTrajectory.add(new Position(x, y - 1));
        reverseTrajectory.add(new Position(x, y - 1));
        reverseTrajectory.add(new Position(x + 1, y));
        reverseTrajectory.add(new Position(x + 1, y));
        reverseTrajectory.add(new Position(x, y - 1));
        reverseTrajectory.add(new Position(x, y + 1));

        int nextRotation = currentRotation % 8 + 1;
        List<Entity> listBoulders = DungeonManiaController.getAllEntities().stream()
                .filter((Entity e) -> e instanceof Boulder).collect(Collectors.toList());
        if (listBoulders.stream()
                .anyMatch((Entity p) -> p.getPosition().equals(movementTrajectory.get(nextRotation)))) {
            if (isReversed) {
                newPosition = new Position(movementTrajectory.get(nextRotation).getX(),
                        movementTrajectory.get(nextRotation).getY());
                isReversed = false;
            } else {
                newPosition = new Position(reverseTrajectory.get(nextRotation).getX(),
                        reverseTrajectory.get(nextRotation).getY());
                isReversed = true;
            }

        } else if (isReversed) {
            newPosition = new Position(movementTrajectory.get(nextRotation).getX(),
                    movementTrajectory.get(nextRotation).getY());
        } else {
            newPosition = new Position(movementTrajectory.get(nextRotation).getX(),
                    movementTrajectory.get(nextRotation).getY());
        }

        currentRotation = nextRotation;

        return newPosition;
    }
}
