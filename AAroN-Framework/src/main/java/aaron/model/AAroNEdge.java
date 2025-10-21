package aaron.model;

import java.util.Objects;

public class AAroNEdge extends WithProperties {

    private String neo4jElementId;
    private String type;
    private Identifier start;
    private Identifier end;

    private AAroNEdge() {
    }

    public String getNeo4jElementId() {
        return neo4jElementId;
    }

    public void setNeo4jElementId(String neo4jElementId) {
        this.neo4jElementId = neo4jElementId;
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


    public <V, T extends PropertyType<V>> Property<V> addProperty(final T type, final String name, final V value) {
        return this.addProperty(name, type, value);
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

        private final AAroNEdge edge;

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

        public <E, T extends PropertyType<E>> Builder addProperty(final String key, final T type, final E value) {
            if (value != null) {
                edge.addProperty(key, type, value);
            }
            return this;
        }

        public AAroNEdge build() {
            return edge;
        }
    }
}
