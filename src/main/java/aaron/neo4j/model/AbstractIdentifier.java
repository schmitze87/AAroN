package aaron.neo4j.model;

import java.util.Objects;

public class AbstractIdentifier<T> implements Identifier<T> {

    private final T id;

    public AbstractIdentifier(final T id) {
        this.id = id;
    }

    @Override
    public T getIdentifier() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIdentifier<?> that = (AbstractIdentifier<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
