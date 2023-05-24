package aaron.archimate.processors;

import aaron.archimate.exchangexml.*;
import aaron.archimate.identifier.ArchiMateIdentifier;
import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.Model;

import java.util.List;

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
        ArchiMateIdentifier diagramIdentifier = new ArchiMateIdentifier(identifier);
        model.addNode(diagramIdentifier, node);

        for (ViewNodeType viewNode : diagram.getNode()) {
            handleViewNode(diagramIdentifier, viewNode, null);
        }
    }

    private void handleViewNode(ArchiMateIdentifier diagramIdentifier, ViewNodeType viewNode, ArchiMateIdentifier parentViewNode) {
        if (viewNode instanceof Label label) {
            //Skip this as it has not much value for the graph. At least for now
            return;
        }
        if (viewNode instanceof Element element) {
            Object elementRef = element.getElementRef();
            ArchiMateIdentifier elementIdentifier = null;
            if (elementRef instanceof DataObject dataObject) {
                elementIdentifier = new ArchiMateIdentifier(dataObject.getIdentifier());
                AAroNEdge edge = AAroNEdge.builder()
                        .setStart(diagramIdentifier)
                        .setEnd(elementIdentifier)
                        .setType("SHOWS")
                        .build();
                model.addEdge(new ArchiMateIdentifier(viewNode.getIdentifier()), edge);
            }
            for (ViewNodeType childViewNode : element.getNode()) {
                handleViewNode(diagramIdentifier, childViewNode, elementIdentifier);
                if (elementIdentifier != null) {
                    AAroNEdge edge = AAroNEdge.builder()
                            .setStart(diagramIdentifier)
                            .setEnd(elementIdentifier)
                            .setType("EMBEDS")
                            .build();
                    model.addEdge(new ArchiMateIdentifier(viewNode.getIdentifier()), edge);
                }
            }
            return;
        }
        if (viewNode instanceof Container container) {
            for (ViewNodeType childViewNode : container.getNode()) {
                handleViewNode(diagramIdentifier, childViewNode, null);
            }
        }
    }
}
