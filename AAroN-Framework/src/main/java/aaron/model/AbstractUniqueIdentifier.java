package aaron.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractUniqueIdentifier<T> implements UniqueIdentifier<T>, Serializable {

    protected final T id;

    public AbstractUniqueIdentifier(T id) {
        this.id = id;
    }

    public T getIdentifier() {
        return id;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
