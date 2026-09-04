package aaron.archimate.processors;

import aaron.archimate.exchangexml.OrganizationType;
import aaron.archimate.exchangexml.ReferenceableType;
import aaron.archimate.identifier.ArchiMateEdgeIdentifier;
import aaron.archimate.identifier.ArchiMateNodeIdentifier;
import aaron.model.*;

import java.util.List;
import java.util.UUID;

import static aaron.model.PropertyType.STRING;

public class OrganisationProcessor extends AbstractProcessor<OrganizationType> {

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
        if (identifierRef instanceof ReferenceableType) {
            ReferenceableType referencedObject = (ReferenceableType) identifierRef;
            identifier = new ArchiMateNodeIdentifier(referencedObject.getIdentifier());
        } else {
            String name = getName(organizationType.getLabelGroup());
            AAroNNode.Builder builder = AAroNNode.builder();
            builder.addLabel("Organization");
            builder.addProperty("name", STRING, name);
            node = builder.build();
            UniqueNodeIdentifier<UUID> uniqueIdentifier = new UniqueNodeIdentifierImpl();
            identifier = new ArchiMateNodeIdentifier(name);
            model.addNode(uniqueIdentifier, node);
            model.addNodeIdentifier(identifier, uniqueIdentifier);
        }
        if (parentId != null) {
            AAroNEdge containsEdge = AAroNEdge.builder()
                    .setStart(parentId)
                    .setEnd(identifier)
                    .setType("CONTAINS")
                    .build();
            UniqueEdgeIdentifier<UUID> uniqueEdgeIdentifier = new UniqueEdgeIdentifierImpl();
            model.addEdge(uniqueEdgeIdentifier, containsEdge);
            model.addEdgeIdentifier(new ArchiMateEdgeIdentifier(UUID.randomUUID().toString()), uniqueEdgeIdentifier);
        }
        List<OrganizationType> itemList = organizationType.getItem();
        for (OrganizationType subOrganizationType : itemList) {
            process(identifier, subOrganizationType);
        }
    }
}
