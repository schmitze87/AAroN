package aaron.model;

@FunctionalInterface
public interface Processor <T>{

    void process(T input);
}
