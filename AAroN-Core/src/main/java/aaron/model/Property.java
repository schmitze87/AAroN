package aaron.model;

public class Property<T> {

    private T value;
    private final PropertyType<T> type;

    public Property(PropertyType<T> type, T value) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public PropertyType<T> getType() {
        return type;
    }
}