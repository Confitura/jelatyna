package pl.confitura.jelatyna;

public class JsonTestUtil {
    public static String json(String s) {
        return s.replaceAll("'", "\"");
    }
}
