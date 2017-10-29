package stream.dojo;

public class Utils {

    public static String sanitize(String word) {
        return word.toLowerCase().replaceAll("[,;:.]+", "").toLowerCase();
    }

}
