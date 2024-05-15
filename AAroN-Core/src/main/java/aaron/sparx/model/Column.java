package aaron.sparx.model;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.text.DateFormatter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                var dateTime = tryGetDateTime(s);
                if (dateTime == null) {
                    dateTime = tryGetDate(s);
                }
                if (dateTime == null) {
                    return (T) LocalDateTime.now();
                } else {
                    return (T) dateTime;
                }
            }
        }
        if (o instanceof Integer && clazz == Boolean.class) {
            return clazz.cast(BooleanUtils.toBoolean((int) o));
        }
        return clazz.cast(o);
    }

    private T tryGetDateTime(String dateStr) {
        List<String> dateTimeFormats = Arrays.asList("yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm", "MM/dd/yyyy HH:mm:ss");
        for (String dateTimeFormat : dateTimeFormats) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            try {
                return (T) LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Parsing fehlgeschlagen, probiere das nächste Format
            }
        }
        return null; // Keines der Formate passte
    }

    private T tryGetDate(String dateStr) {
        List<String> dateFormats = Arrays.asList("yyyy-MM-dd", "dd.MM.yyyy", "MM/dd/yyyy");
        for (String dateFormat : dateFormats) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            try {
                var localDate = LocalDate.parse(dateStr, formatter);
                return (T) LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
            } catch (DateTimeParseException e) {
                // Parsing fehlgeschlagen, probiere das nächste Format
            }
        }
        return null; // Keines der Formate passte
    }
}
