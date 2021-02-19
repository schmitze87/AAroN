package aaron.sparx.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class Column<T> {

    private final String name;
    private final Class<T> clazz;

    Column(final String name, final Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public <U extends Map<String, Object>> T value (final U map) {
        Object o = map.get(name);
        if (o instanceof Date) {
            Date date = (Date) o;
            LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return clazz.cast(localDateTime);
        }
        return clazz.cast(o);
    }

}
