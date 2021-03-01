package aaron.model;

import java.util.*;

public class AAroNNode extends WithProperties {

    private Set<String> labels;

    private AAroNNode() {
        labels = new HashSet<>();
    }

    public String getName() {
        return (String) properties.get("name");
    }

    public boolean addLabel(String label) {
        if (label != null) {
            return labels.add(label);
        }
        return false;
    }

    public Set<String> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AAroNNode aAroNNode = (AAroNNode) o;
        return labels.equals(aAroNNode.labels) && properties.equals(aAroNNode.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labels, properties);
    }

    public static class Builder {

        private AAroNNode node;

        private Builder() {
            node = new AAroNNode();
        }

        public Builder addLabel(final String label) {
            if (label != null) {
                node.addLabel(label);
            }
            return this;
        }

        public Builder addProperty(final String key, final Object value) {
            if (value != null) {
                node.addProperty(key, value);
            }
            return this;
        }

        public AAroNNode build() {
            return node;
        }
    }
}
