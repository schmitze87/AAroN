package aaron.archimate.processors;

import aaron.archimate.exchangexml.ElementType;
import aaron.archimate.identifier.ArchiMateIdentifier;
import aaron.model.AAroNNode;
import aaron.model.Model;

public class ElementProcessor extends AbstractProcessor<ElementType>{

    public ElementProcessor(final Model model) {
        super(model);
    }

    public void process(ElementType element) {
        String type = element.getClass().getSimpleName();
        String identifier = element.getIdentifier();
        String name = getName(element.getNameGroup());
        String documentation = getDocumentation(element.getDocumentation());

        AAroNNode.Builder builder = AAroNNode.builder();
        builder.addLabel(type);
        builder.addProperty("name", name);
        builder.addProperty("documentation", documentation);

        processProperties(element.getProperties(), builder::addProperty);
        AAroNNode node = builder.build();
        model.addNode(new ArchiMateIdentifier(identifier), node);
    }
}
