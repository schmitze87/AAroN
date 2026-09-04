package aaron.archimate.processors;

import aaron.archimate.exchangexml.Diagram;
import aaron.archimate.exchangexml.LangStringType;
import aaron.archimate.identifier.ArchiMateNodeIdentifier;
import aaron.model.AAroNNode;
import aaron.model.Model;
import aaron.model.UniqueNodeIdentifier;
import aaron.model.UniqueNodeIdentifierImpl;

import java.util.List;
import java.util.UUID;

import static aaron.model.PropertyType.STRING;

public class DiagramProcessor extends AbstractProcessor<Diagram> {

    public DiagramProcessor(Model model) {
        super(model);
    }

    @Override
    public void process(Diagram diagram) {
        List<LangStringType> nameGroup = diagram.getNameGroup();
        String name = null;
        if (!nameGroup.isEmpty()) {
            name = nameGroup.get(0).getValue();
        }
        String identifier = diagram.getIdentifier();

        AAroNNode node = AAroNNode.builder()
                .addLabel("Diagram")
                .addProperty("name", STRING, name)
                .addProperty("identifier", STRING, identifier)
                .build();
        UniqueNodeIdentifier<UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
        model.addNode(uniqueNodeIdentifier, node);
        model.addNodeIdentifier(new ArchiMateNodeIdentifier(identifier), uniqueNodeIdentifier);
    }
}
