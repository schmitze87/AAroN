package aaron.sparx.model;

public interface EAGuid {

    static boolean isEaGuid(String s) {
        return s.matches("\\{\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}\\}");
    }
}
