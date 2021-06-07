package aaron.sparx.identifiers;

import java.util.Objects;

public class Tuple<T1, T2> {

    private final T1 obj1;
    private final T2 obj2;

    public Tuple(final T1 obj1, final T2 obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(obj1, tuple.obj1) && Objects.equals(obj2, tuple.obj2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj1, obj2);
    }
}
