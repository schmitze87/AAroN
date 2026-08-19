package aaron.model;

import java.util.Objects;
import java.util.UUID;

public class UniqueNodeIdentifierImpl extends AbstractUniqueIdentifier<UUID> implements UniqueNodeIdentifier<UUID> {

    public UniqueNodeIdentifierImpl() {
        super(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "UniqueNodeIdentifier{" +
                "id=" + id.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UniqueNodeIdentifierImpl that = (UniqueNodeIdentifierImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
