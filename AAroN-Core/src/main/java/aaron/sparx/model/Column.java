package aaron.sparx.model;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public <U extends Map<String, Object>> T value(final U map) {
        Object o = map.get(name);
        if (o instanceof Date) {
            Date date = (Date) o;
            LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return clazz.cast(localDateTime);
        }
        if (o instanceof String) {
            String s = (String) o;
            if (clazz == Boolean.class) {
                if (StringUtils.isNumeric(s)) {
                    return clazz.cast(BooleanUtils.toBoolean(Integer.parseInt(s)));
                } else {
                    return clazz.cast(BooleanUtils.toBooleanObject((String) o));
                }
            }
            if (clazz == LocalDateTime.class) {
                try {
                    return (T) LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (DateTimeParseException e1) {
                    var localDate = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    try {
                        return (T) LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
                    } catch (DateTimeParseException e2) {
                        return (T) LocalDateTime.now();
                    }
                }
            }
        }
        if (o instanceof Integer && clazz == Boolean.class) {
            return clazz.cast(BooleanUtils.toBoolean((int) o));
        }
        return clazz.cast(o);
    }

}
