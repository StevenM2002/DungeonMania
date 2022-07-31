package dungeonmania.MovingEntities;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import static dungeonmania.DungeonManiaController.getDmc;
import dungeonmania.util.Direction;

public class OldPlayerMovement extends Movement {
    Queue<Direction> toMoveQueue;
    

    public OldPlayerMovement(JSONArray ticks, int currentTick) {
        toMoveQueue = new LinkedBlockingQueue<>();
        for (int i = currentTick + 1; i < ticks.length(); i++) {
            if (ticks.getJSONObject(i).has("moved")) {
                switch (ticks.getJSONObject(i).getString("moved")) {
                    case "UP":
                        toMoveQueue.add(Direction.UP);
                        break;
                    case "DOWN":
                        toMoveQueue.add(Direction.DOWN);
                        break;
                    case "LEFT":
                        toMoveQueue.add(Direction.LEFT);
                        break;
                    case "RIGHT":
                        toMoveQueue.add(Direction.RIGHT);
                        break;
                }
            } else {
                toMoveQueue.add(null);
            }
        }
    }


    @Override
    public void moveEntity(MovingEntity entity) {
        // moves the older player, removes the player if there is no longer a movement option
        try {
            Direction toMove = toMoveQueue.remove();
            if (toMove != null) {
                entity.move(toMove);
            }
        } catch (NoSuchElementException e){
            getDmc().getAllEntities().remove(entity);
        }
    }
    
}
