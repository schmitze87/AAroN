package aaron.model;

import java.time.LocalDateTime;

public class PropertyType<E> {

    public static PropertyType<String> STRING = new PropertyType<>("string", String.class);
    public static PropertyType<String[]> STRING_ARRAY = new PropertyType<>("string[]", String[].class);
    public static PropertyType<Integer> INTEGER = new PropertyType<>("int", int.class);
    public static PropertyType<Integer[]> INTEGER_ARRAY = new PropertyType<>("int[]", Integer[].class);
    public static PropertyType<Boolean> BOOLEAN = new PropertyType<>("boolean", boolean.class);
    public static PropertyType<Boolean[]> BOOLEAN_ARRAY = new PropertyType<>("boolean[]", Boolean[].class);
    public static PropertyType<Long> LONG = new PropertyType<>("long", long.class);
    public static PropertyType<Long[]> LONG_ARRAY = new PropertyType<>("long[]", Long[].class);
    public static PropertyType<Short> SHORT = new PropertyType<>("short", short.class);
    public static PropertyType<Short[]> SHORT_ARRAY = new PropertyType<>("short[]", Short[].class);
    public static PropertyType<Double> DOUBLE = new PropertyType<>("double", double.class);
    public static PropertyType<Double[]> DOUBLE_ARRAY = new PropertyType<>("double[]", Double[].class);
    public static PropertyType<Float> FLOAT = new PropertyType<>("float", float.class);
    public static PropertyType<Float[]> FLOAT_ARRAY = new PropertyType<>("float[]", Float[].class);
    public static PropertyType<LocalDateTime> LOCALDATETIME = new PropertyType<>("localdatetime", LocalDateTime.class);
    public static PropertyType<LocalDateTime[]> LOCALDATETIME_ARRAY = new PropertyType<>("localdatetime[]", LocalDateTime[].class);

    public static PropertyType[] TYPES = new PropertyType[]{STRING, STRING_ARRAY, INTEGER, INTEGER_ARRAY,
            BOOLEAN, BOOLEAN_ARRAY, LONG, LONG_ARRAY, SHORT, SHORT_ARRAY, DOUBLE, DOUBLE_ARRAY, FLOAT, FLOAT_ARRAY,
            LOCALDATETIME, LOCALDATETIME_ARRAY};

    private String csvValue;
    private Class<E> clazz;

    private PropertyType(String csvValue, Class<E> clazz) {
        this.csvValue = csvValue;
        this.clazz = clazz;
    }

    public String getCsvValue() {
        return csvValue;
    }

    public Class<E> getClazz() {
        return clazz;
    }

    public E cast(Object value) {
        return clazz.cast(value);
    }
}
