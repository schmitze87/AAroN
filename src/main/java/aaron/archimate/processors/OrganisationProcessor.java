package aaron.archimate.processors;

import aaron.archimate.exchangexml.OrganizationType;
import aaron.archimate.exchangexml.ReferenceableType;
import aaron.archimate.identifier.ArchiMateIdentifier;
import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Identifier;
import aaron.model.Model;

import java.util.List;
import java.util.UUID;

public class OrganisationProcessor extends AbstractProcessor<OrganizationType>{

    public OrganisationProcessor(Model model) {
        super(model);
    }

    @Override
    public void process(OrganizationType organizationType) {
        this.process(null, organizationType);
    }

    public void process(final Identifier parentId, final OrganizationType organizationType) {
        AAroNNode node;
        Identifier identifier;
        Object identifierRef = organizationType.getIdentifierRef();
        if (identifierRef != null && identifierRef instanceof ReferenceableType) {
            ReferenceableType referencedObject = (ReferenceableType) identifierRef;
            identifier = new ArchiMateIdentifier(referencedObject.getIdentifier());
        } else {
            String name = getName(organizationType.getLabelGroup());
            AAroNNode.Builder builder = AAroNNode.builder();
            builder.addLabel("Organization");
            builder.addProperty("name", name);
            node = builder.build();
            identifier = new ArchiMateIdentifier(name);
            model.addNode(identifier, node);
        }
        if (parentId != null) {
            Edge containsEdge = Edge.builder()
                    .setStart(parentId)
                    .setEnd(identifier)
                    .setType("CONTAINS")
                    .build();
            model.addEdge(new ArchiMateIdentifier(UUID.randomUUID().toString()), containsEdge);
        }
        List<OrganizationType> itemList = organizationType.getItem();
        for (OrganizationType subOrganizationType : itemList) {
            process(identifier, subOrganizationType);
        }
    }
}
