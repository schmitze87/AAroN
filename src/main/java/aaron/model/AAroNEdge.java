package aaron.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AAroNEdge extends WithProperties {

    private String type;
    private Identifier start;
    private Identifier end;
    private Map<String, Object> properties;

    private AAroNEdge() {
        properties = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Identifier getStart() {
        return start;
    }

    public void setStart(final Identifier start) {
        this.start = start;
    }

    public Identifier getEnd() {
        return end;
    }

    public void setEnd(final Identifier end) {
        this.end = end;
    }


    public Object addProperty(final String name, final Object value) {
        return properties.put(name, value);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AAroNEdge edge = (AAroNEdge) o;
        return type.equals(edge.type) && start.equals(edge.start) && end.equals(edge.end) && properties.equals(edge.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, start, end, properties);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private AAroNEdge edge;

        private Builder() {
            edge = new AAroNEdge();
        }

        public Builder setType(final String type) {
            edge.type = type;
            return this;
        }

        public Builder setStart(final Identifier node) {
            edge.start = node;
            return this;
        }

        public Builder setEnd(final Identifier node) {
            edge.end = node;
            return this;
        }

        public Builder addProperty(final String key, final Object value) {
            if (value != null) {
                edge.properties.put(key, value);
            }
            return this;
        }

        public AAroNEdge build() {
            return edge;
        }
    }
}
