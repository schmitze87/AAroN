package aaron.sparx;

import java.util.regex.Pattern;

public class GUIDHelper {

    private final static Pattern guidPattern = Pattern.compile("\\{\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}\\}");

    public static boolean isEaGuid(String s) {
        return s != null ? guidPattern.matcher(s).matches() : false;
    }

    public static String unwrapGuid(final String s) {
        if (isEaGuid(s)) {
            return s.substring(1, s.length() -1);
        }
        return s;
    }
}
