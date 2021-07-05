package aaron.model;

import java.util.HashMap;
import java.util.Map;

public class WithProperties {

    protected final Map<String, Property> properties = new HashMap<>();

    public Map<String, Property> getProperties() {
        return properties;
    }

//    public Object addProperty(final String name, final Object value) {
//        if (value instanceof Collection) {
//            Collection c = (Collection) value;
//            return properties.put(name, c.toArray());
//        } else {
//            return properties.put(name, value);
//        }
//    }

    public <T>Property<T> addProperty(final String name, final PropertyType<T> type, final T value) {
        Property<T> property = new Property<>(type, value);
        return properties.put(name, property);
    }

    public <T, E extends PropertyType<T>> T getProperty(E type, final String name) {
        Property property = properties.get(name);
        return type.cast(property.getValue());
    }
}
