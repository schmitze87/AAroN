package aaron.archimate.processors;

import aaron.archimate.exchangexml.ElementType;
import aaron.archimate.exchangexml.PropertiesType;
import aaron.archimate.exchangexml.RelationshipType;
import aaron.archimate.identifier.ArchiMateIdentifier;
import aaron.model.AAroNEdge;
import aaron.model.Identifier;
import aaron.model.Model;

import static aaron.model.PropertyType.STRING;

public class RelationshipProcessor extends AbstractProcessor<RelationshipType> {

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

        AAroNEdge.Builder builder = AAroNEdge.builder();
        builder.setType(type);
        builder.setStart(start);
        builder.setEnd(end);
        builder.addProperty("documentation", STRING, documentation);
        builder.addProperty("name", STRING, name);
        processProperties(properties, builder::addProperty);
        AAroNEdge edge = builder.build();
        model.addEdge(new ArchiMateIdentifier(identifier), edge);
    }
}
