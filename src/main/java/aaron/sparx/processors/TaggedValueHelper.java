package aaron.sparx.processors;

import aaron.model.*;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.ConnectorId;
import aaron.sparx.identifiers.ImplizitRelationId;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaggedValueHelper {

    static void process(final String sha1, final LocalDateTime time, final Model model, final TaggedValueMode mode, final String name, final String value,
                        final Identifier<String> tagIdentifier, final Identifier taggedElementIdentifier) {
        AAroNNode node = null;
        switch (mode) {
            case AS_NODE:
                node = AAroNNode.builder().build();
                node.addLabel("TaggedValue");
                node.addProperty("name", name);
                node.addProperty("value", value);
                node.addProperty("eaGuid", tagIdentifier.getIdentifier());
                node.addProperty("eapHash", sha1);
                node.addProperty("importedAt", time);

                model.addNode(tagIdentifier, node);

                //CONNECT WITH OBJECT
                Edge edge = Edge.builder()
                        .setType("HAS_TAGGED_VALUE")
                        .setStart(taggedElementIdentifier)
                        .setEnd(tagIdentifier)
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), edge);
                break;
            case AS_PROPERTY:
                WithProperties withProperties;
                if (taggedElementIdentifier instanceof ConnectorId) {
                    withProperties = model.getEdge(taggedElementIdentifier);
                } else {
                    withProperties = model.getNode(taggedElementIdentifier);
                }
                if (withProperties != null) {
                    withProperties.addProperty("tag_" + name, value);
                }
                break;
        }
    }
}
