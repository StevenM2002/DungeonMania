package dungeonmania.util;

import java.util.List;
import java.util.Arrays;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final Position offset;

    private Direction(Position offset) {
        this.offset = offset;
    }

    private Direction(int x, int y) {
        this.offset = new Position(x, y);
    }

    public Position getOffset() {
        return this.offset;
    }

    public Direction reverse() {
        if (this == UP) {
            return DOWN;
        } else if (this == DOWN) {
            return UP;
        } else if (this == LEFT) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    public static final List<Direction> allDirections() {
        return Arrays.asList(UP, LEFT, RIGHT, DOWN);
    }
}
