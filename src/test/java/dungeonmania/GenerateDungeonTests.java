package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GenerateDungeonTests {
    private int spaces = 0;

    public int getSpaces() {
        return spaces;
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }
    
    private List<int[]> createDirections() {
        List<int[]> directions = new ArrayList<>();
        int[] temp = new int[] {1,0};
        directions.add(temp);
        temp = new int[] {-1,0};
        directions.add(temp);
        temp = new int[] {0,1};
        directions.add(temp);
        temp = new int[] {0,-1};
        directions.add(temp);

        return directions;
    }
    
    private boolean isValidPosition(Position curr, Position start, Position end) {
        return curr.getX() >= start.getX() && curr.getX() <= end.getX() && curr.getY() >= start.getY() && curr.getY() <= end.getY();
    }

    private boolean hasValidMove(List<EntityResponse> entities, boolean [][] visited, Position curr, Position start) {
        List<int[]> directions = createDirections();

        for (int[] d : directions) {
            int newRow = curr.getX() + d[0];
            int newCol = curr.getY() + d[1];
            Position newPosition = new Position(newRow, newCol);
            boolean isEntity = entities.stream().anyMatch(e -> e.getPosition().equals(newPosition) && e.getType().equals("wall"));
            if (!isEntity && 
                !visited[newRow - start.getX()][newCol - start.getY()] && 
                isValidPosition(newPosition, start, new Position(start.getX() + visited[0].length, start.getY() + visited[0].length))) {
                return true;
            }
            
        }
        return false;
    }

    private List<Position> getNextMove(List<EntityResponse> entities, boolean [][] visited, Position curr, Position start) {
        List<int[]> directions = createDirections();
        List<Position> positions = new ArrayList<>();

        for (int[] d : directions) {
            int newRow = curr.getX() + d[0];
            int newCol = curr.getY() + d[1];
            Position newPosition = new Position(newRow, newCol);
            boolean isEntity = entities.stream().anyMatch(e -> e.getPosition().equals(newPosition) && e.getType().equals("wall"));
            if (!isEntity && 
                !visited[newRow - start.getX()][newCol - start.getY()] &&
                isValidPosition(newPosition, start, new Position(start.getX() + visited[0].length, start.getY() + visited[0].length))) {
                positions.add(newPosition);
            }
            
        }
        return positions;
    }

    private boolean doDFS(List<EntityResponse> entities, boolean [][] visited, Position start, Position end, Stack<Position> stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Position curr = stack.pop();
        visited[curr.getX() - start.getX()][curr.getY() - start.getY()] = true;
        if (hasValidMove(entities, visited, curr, start)) {
            List<Position> positions = getNextMove(entities, visited, curr, start);
            Position next = positions.get(0);
            setSpaces(getSpaces() + 1);
            if (next.equals(end)) {
                return true;
            }
            for (Position p : positions) {
                stack.push(p);
            }
        }
        return doDFS(entities, visited, start, end, stack);
    }

    private boolean basicDFS(List<EntityResponse> entities) {
        Position start = entities.stream().filter(e -> e.getType().equals("player")).findFirst().get().getPosition();
        Position end = entities.stream().filter(e -> e.getType().equals("exit")).findFirst().get().getPosition();
        boolean [][] visited = generateGraph(start, end);
        Stack<Position> stack = new Stack<>();
        stack.push(start);
        return doDFS(entities, visited, start, end, stack);
    }

    private boolean[][] generateGraph(Position start, Position end) {
        return new boolean[end.getX() - start.getX() + 1][end.getY() - start.getY() + 1];
    }

    @Test
    public void testEvenSize() {
        for (int i = 0; i < 100; i++) {
            DungeonManiaController d = new DungeonManiaController();
            DungeonResponse mazeRes = d.generateDungeon(1, 1, 11, 11, "c_Test_SpiderConfig");
            assertTrue(basicDFS(mazeRes.getEntities()));
        }
    }

    @Test
    public void testOddSize() {
        for (int i = 0; i < 100; i++) {
            DungeonManiaController d = new DungeonManiaController();
            DungeonResponse mazeRes = d.generateDungeon(1, 1, 10, 10, "c_Test_SpiderConfig");
            assertTrue(basicDFS(mazeRes.getEntities()));
        }
    }
}
