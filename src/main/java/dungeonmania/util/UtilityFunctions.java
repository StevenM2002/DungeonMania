package dungeonmania.util;

public class UtilityFunctions {
    public static String camelToSnake(String s) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return s
            .replaceAll(regex, replacement)
            .toLowerCase();
    }
}
