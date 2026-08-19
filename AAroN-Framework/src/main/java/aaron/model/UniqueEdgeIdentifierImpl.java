package aaron.model;

import java.util.Objects;
import java.util.UUID;

public class UniqueEdgeIdentifierImpl extends AbstractUniqueIdentifier<UUID> implements UniqueEdgeIdentifier<UUID> {

    public UniqueEdgeIdentifierImpl() {
        super(UUID.randomUUID());
    }

    public UniqueEdgeIdentifierImpl(UUID id) {
        super(id);
    }

    @Override
    public String toString() {
        return "UniqueEdgeIdentifier{" +
                "id=" + id.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UniqueEdgeIdentifierImpl that = (UniqueEdgeIdentifierImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
