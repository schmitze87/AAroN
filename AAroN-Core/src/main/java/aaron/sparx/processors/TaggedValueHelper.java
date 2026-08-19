package aaron.sparx.processors;

import aaron.logging.Logger;
import aaron.model.*;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.ConnectorId;
import aaron.sparx.identifiers.ImplizitRelationId;

import java.time.LocalDateTime;

import static aaron.model.PropertyType.LOCALDATETIME;
import static aaron.model.PropertyType.STRING;

public class TaggedValueHelper {

    @SuppressWarnings("rawtypes")
    static void process(final String sha1, final LocalDateTime time, final Model model, final TaggedValueMode mode, final String name, final String value,
                        final Identifier<String> tagIdentifier, final Identifier taggedElementIdentifier, Logger logger) {
        AAroNNode node;
        switch (mode) {
            case AS_NODE:
                node = AAroNNode.builder().build();
                node.addLabel("TaggedValue");
                node.addProperty("name", STRING, name);
                node.addProperty("value", STRING, value);
                node.addProperty("eaGuid", STRING, tagIdentifier.getIdentifier());
                node.addProperty("eapHash", STRING, sha1);
                node.addProperty("importedAt", LOCALDATETIME, time);
                UniqueNodeIdentifier<java.util.UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
                model.addNode(uniqueNodeIdentifier, node);
                model.addNodeIdentifier(tagIdentifier, uniqueNodeIdentifier);

                //CONNECT WITH OBJECT
                AAroNEdge edge = AAroNEdge.builder()
                        .setType("HAS_TAGGED_VALUE")
                        .setStart(taggedElementIdentifier)
                        .setEnd(tagIdentifier)
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(), edge);
                break;
            case AS_PROPERTY:
                if (taggedElementIdentifier instanceof ConnectorId) {
                    UniqueEdgeIdentifier<java.util.UUID> uniqueEdgeIdentifier = model.getUniqueEdgeIdentifier(taggedElementIdentifier);
                    AAroNEdge taggedEdge = model.getEdge(uniqueEdgeIdentifier);
                    if (taggedEdge != null) {
                        taggedEdge.addProperty("tag_" + name, STRING, value);
                    }
                    model.addEdge(uniqueEdgeIdentifier, taggedEdge);
                } else {
                    UniqueNodeIdentifier<java.util.UUID> uniqueTaggedNodeIdentifier = model.getUniqueNodeIdentifier(taggedElementIdentifier);
                    AAroNNode taggedNode = model.getNode(uniqueTaggedNodeIdentifier);
                    if (taggedNode != null) {
                        taggedNode.addProperty("tag_" + name, STRING, value);
                    }
                    model.addNode(uniqueTaggedNodeIdentifier, taggedNode);
                }
                break;
        }
    }
}
