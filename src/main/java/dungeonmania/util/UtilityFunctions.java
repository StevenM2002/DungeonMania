package dungeonmania.util;

import java.util.ArrayList;


public class UtilityFunctions {
    public static String camelToSnake(String s) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return s
            .replaceAll(regex, replacement)
            .toLowerCase();
    }

    public static ArrayList<Direction> getAllDirections() {
        ArrayList<Direction> allDirections = new ArrayList<>();
        allDirections.add(Direction.UP);
        allDirections.add(Direction.DOWN);
        allDirections.add(Direction.LEFT);
        allDirections.add(Direction.RIGHT);
        return allDirections;
    }
}
