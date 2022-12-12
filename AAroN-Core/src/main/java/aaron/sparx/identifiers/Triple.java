package aaron.sparx.identifiers;

import java.util.Objects;

public class Triple<T1, T2, T3> {

    private final T1 obj1;
    private final T2 obj2;
    private final T3 obj3;

    public Triple(final T1 obj1, final T2 obj2, final T3 obj3) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj3 = obj3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(obj1, triple.obj1) && Objects.equals(obj2, triple.obj2) && Objects.equals(obj3, triple.obj3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj1, obj2, obj3);
    }
}
