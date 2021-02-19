package aaron.sparx;

import java.util.Map;

@FunctionalInterface
public interface Processor {

    <U extends Map<String, Object>> void process(final U row);
}
