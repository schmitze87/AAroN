package aaron.neo4j.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WithProperties {

    protected final Map<String, Object> properties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object addProperty(final String name, final Object value) {
        if (value instanceof Collection) {
            Collection c = (Collection) value;
            return properties.put(name, c.toArray());
        } else {
            return properties.put(name, value);
        }
    }
}
