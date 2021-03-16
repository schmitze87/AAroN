package aaron.archimate.processors;

import aaron.archimate.exchangexml.ElementType;
import aaron.archimate.exchangexml.PropertiesType;
import aaron.archimate.exchangexml.RelationshipType;
import aaron.archimate.identifier.ArchiMateIdentifier;
import aaron.model.Edge;
import aaron.model.Identifier;
import aaron.model.Model;

public class RelationshipProcessor extends AbstractProcessor<RelationshipType>{

    public RelationshipProcessor(Model model) {
        super(model);
    }

    @Override
    public void process(RelationshipType relationshipType) {
        String identifier = relationshipType.getIdentifier();
        String documentation = getDocumentation(relationshipType.getDocumentation());
        String name = getName(relationshipType.getNameGroup());
        ElementType source = (ElementType) relationshipType.getSource();
        ElementType target = (ElementType) relationshipType.getTarget();
        String type = relationshipType.getClass().getSimpleName();
        PropertiesType properties = relationshipType.getProperties();

        Identifier start = new ArchiMateIdentifier(source.getIdentifier());
        Identifier end = new ArchiMateIdentifier(target.getIdentifier());

        Edge.Builder builder = Edge.builder();
        builder.setType(type);
        builder.setStart(start);
        builder.setEnd(end);
        builder.addProperty("documentation", documentation);
        builder.addProperty("name", name);
        processProperties(properties, builder::addProperty);
        Edge edge = builder.build();
        model.addEdge(new ArchiMateIdentifier(identifier), edge);
    }
}
