package aaron.sparx.processors;

import aaron.model.Model;

import java.util.Map;

public class AttributeProcessor implements Processor {

    private final Model model;

    public AttributeProcessor(final Model model) {
        this.model = model;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {

    }
}
