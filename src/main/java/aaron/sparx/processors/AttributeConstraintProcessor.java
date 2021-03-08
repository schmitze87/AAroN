package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.identifiers.AttributeConstraint;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.Tuple;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.sparx.model.EAAttributeConstraint.*;


public class AttributeConstraintProcessor extends AbstractProcessor{

    public AttributeConstraintProcessor(final String sha1, final LocalDateTime time, final Model model) {
        super(sha1, time, model);
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        Integer objectId = OBJECT_ID.value(row);
        String constraint = CONSTRAINT.value(row);
        String attributeName = ATT_NAME.value(row);
        String type = TYPE.value(row);
        String notes = NOTES.value(row);
        Integer id = ID.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("ATTRIBUTE_CONSTRAINT")
                .addLabel("CONSTRAINT")
                .addProperty("constraint", constraint)
                .addProperty("attributeName", attributeName)
                .addProperty("type", type)
                .addProperty("notes", notes)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();

        AttributeId attributeId = new AttributeId(id);
        AttributeConstraint identifier = new AttributeConstraint(new Tuple<>(constraint, attributeId));
        model.addNode(identifier, node);

        Edge edge = Edge.builder()
                .setType("HAS_CONSTRAINT")
                .setStart(attributeId)
                .setEnd(identifier)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), edge);
    }
}
