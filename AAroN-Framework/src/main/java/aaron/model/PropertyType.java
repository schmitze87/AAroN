package aaron.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import aaron.model.spatial.WGS84Point;

public class PropertyType<E extends Serializable> implements Serializable {

    public static PropertyType<Byte> BYTE = new PropertyType<>("byte", Byte.class);
    public static PropertyType<Byte[]> BYTE_ARRAY = new PropertyType<>("byte", Byte[].class);
    public static PropertyType<Character> CHAR = new PropertyType<>("char", Character.class);
    public static PropertyType<Character[]> CHAR_ARRAY = new PropertyType<>("char[]", Character[].class);
    public static PropertyType<String> STRING = new PropertyType<>("string", String.class);
    public static PropertyType<String[]> STRING_ARRAY = new PropertyType<>("string[]", String[].class);
    public static PropertyType<Integer> INTEGER = new PropertyType<>("long", Integer.class);
    public static PropertyType<Integer[]> INTEGER_ARRAY = new PropertyType<>("long[]", Integer[].class);
    public static PropertyType<Boolean> BOOLEAN = new PropertyType<>("boolean", Boolean.class);
    public static PropertyType<Boolean[]> BOOLEAN_ARRAY = new PropertyType<>("boolean[]", Boolean[].class);
    public static PropertyType<Long> LONG = new PropertyType<>("long", Long.class);
    public static PropertyType<Long[]> LONG_ARRAY = new PropertyType<>("long[]", Long[].class);
    public static PropertyType<Short> SHORT = new PropertyType<>("short", Short.class);
    public static PropertyType<Short[]> SHORT_ARRAY = new PropertyType<>("short[]", Short[].class);
    public static PropertyType<Double> DOUBLE = new PropertyType<>("double", Double.class);
    public static PropertyType<Double[]> DOUBLE_ARRAY = new PropertyType<>("double[]", Double[].class);
    public static PropertyType<Float> FLOAT = new PropertyType<>("float", Float.class);
    public static PropertyType<Float[]> FLOAT_ARRAY = new PropertyType<>("float[]", Float[].class);
    public static PropertyType<LocalTime> LOCALTIME = new PropertyType<>("localtime", LocalTime.class);
    public static PropertyType<LocalTime[]> LOCALTIME_ARRAY = new PropertyType<>("localtime[]", LocalTime[].class);
    public static PropertyType<LocalDate> LOCALDATE = new PropertyType<>("date", LocalDate.class);
    public static PropertyType<LocalDate[]> LOCALDATE_ARRAY = new PropertyType<>("date[]", LocalDate[].class);
    public static PropertyType<LocalDateTime> LOCALDATETIME = new PropertyType<>("localdatetime", LocalDateTime.class);
    public static PropertyType<LocalDateTime[]> LOCALDATETIME_ARRAY = new PropertyType<>("localdatetime[]", LocalDateTime[].class);
    public static PropertyType<Duration> DURATION = new PropertyType<>("duration", Duration.class);
    public static PropertyType<Duration[]> DURATION_ARRAY = new PropertyType<>("duration[]", Duration[].class);
    public static PropertyType<WGS84Point> POINT = new PropertyType<>("point{crs:WGS-84}", WGS84Point.class);
    public static PropertyType<WGS84Point[]> POINT_ARRAY = new PropertyType<>("point[]{crs:WGS-84}", WGS84Point[].class);

    public static PropertyType[] TYPES = new PropertyType[]{STRING, STRING_ARRAY, INTEGER, INTEGER_ARRAY,
            BOOLEAN, BOOLEAN_ARRAY, LONG, LONG_ARRAY, SHORT, SHORT_ARRAY, DOUBLE, DOUBLE_ARRAY, FLOAT, FLOAT_ARRAY,
            LOCALDATETIME, LOCALDATETIME_ARRAY, POINT};

    private final String csvValue;
    private final Class<? extends E> clazz;

    private PropertyType(String csvValue, Class<? extends E> clazz) {
        this.csvValue = csvValue;
        this.clazz = clazz;
    }

    public String getCsvValue() {
        return csvValue;
    }

    public Class<? extends E> getClazz() {
        return clazz;
    }

    public E cast(Object value) {
        return clazz.cast(value);
    }
}
