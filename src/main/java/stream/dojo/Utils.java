package stream.dojo;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class Utils {

    public static String sanitize(String word) {
        return word.toLowerCase().replaceAll("[,;:.]+", "").toLowerCase();
    }

    public static boolean isDouble(String word) {
        return word.matches("^[-,+]?\\d+\\.\\d*$");
    }

    public static boolean isInteger(String word) {
        return word.matches("^[-,+]?\\d+$");
    }

}
