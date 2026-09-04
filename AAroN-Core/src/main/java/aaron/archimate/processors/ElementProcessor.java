package aaron.archimate.processors;

import aaron.archimate.exchangexml.ElementType;
import aaron.archimate.identifier.ArchiMateNodeIdentifier;
import aaron.model.AAroNNode;
import aaron.model.Model;
import aaron.model.UniqueNodeIdentifier;
import aaron.model.UniqueNodeIdentifierImpl;

import java.util.UUID;

import static aaron.model.PropertyType.STRING;

public class ElementProcessor extends AbstractProcessor<ElementType> {

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
        builder.addProperty("name", STRING, name);
        builder.addProperty("documentation", STRING, documentation);

        processProperties(element.getProperties(), builder::addProperty);
        AAroNNode node = builder.build();
        UniqueNodeIdentifier<UUID> uniqueIdentifier = new UniqueNodeIdentifierImpl();
        model.addNode(uniqueIdentifier, node);
        model.addNodeIdentifier(new ArchiMateNodeIdentifier(identifier), uniqueIdentifier);
    }
}
