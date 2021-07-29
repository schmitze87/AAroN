package aaron.model;

import org.neo4j.graphdb.spatial.CRS;
import org.neo4j.graphdb.spatial.Coordinate;
import org.neo4j.graphdb.spatial.Point;

import java.time.LocalDateTime;
import java.util.List;

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
    public static PropertyType<Point> POINT = new PropertyType<Point>("point", WGS84Point.class);

    public static PropertyType[] TYPES = new PropertyType[]{STRING, STRING_ARRAY, INTEGER, INTEGER_ARRAY,
            BOOLEAN, BOOLEAN_ARRAY, LONG, LONG_ARRAY, SHORT, SHORT_ARRAY, DOUBLE, DOUBLE_ARRAY, FLOAT, FLOAT_ARRAY,
            LOCALDATETIME, LOCALDATETIME_ARRAY, POINT};

    private String csvValue;
    private Class<? extends E> clazz;

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

    public static class WGS84Point implements Point {

        private final double lat, lon;
        private final Coordinate coordinate;

        public WGS84Point(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            coordinate = new Coordinate(lat, lon);
        }

        @Override
        public List<Coordinate> getCoordinates() {
            return List.of(coordinate);
        }

        @Override
        public CRS getCRS() {
            return new CRS() {
                @Override
                public int getCode() {
                    return 4326;
                }

                @Override
                public String getType() {
                    return "WGS-84";
                }

                @Override
                public String getHref() {
                    return "http://spatialreference.org/ref/epsg/4326/";
                }
            };
        }
    }
}
