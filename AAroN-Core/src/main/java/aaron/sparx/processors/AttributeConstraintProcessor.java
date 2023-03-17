package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.AttributeConstraint;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.Tuple;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.LOCALDATETIME;
import static aaron.model.PropertyType.STRING;
import static aaron.sparx.model.EAAttributeConstraint.*;

public class AttributeConstraintProcessor extends AbstractProcessor {

    public AttributeConstraintProcessor(final String sha1, final LocalDateTime time, final Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer objectId = OBJECT_ID.value(row);
        String constraint = CONSTRAINT.value(row);
        String attributeName = ATT_NAME.value(row);
        String type = TYPE.value(row);
        String notes = NOTES.value(row);
        Integer id = ID.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("ATTRIBUTE_CONSTRAINT")
                .addLabel("CONSTRAINT")
                .addProperty("constraint", STRING, constraint)
                .addProperty("attributeName", STRING, attributeName)
                .addProperty("type", STRING, type)
                .addProperty("notes", STRING, notes)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();

        AttributeId attributeId = new AttributeId(id);
        AttributeConstraint identifier = new AttributeConstraint(new Tuple<>(constraint, attributeId));
        model.addNode(identifier, node);

        AAroNEdge edge = AAroNEdge.builder()
                .setType("HAS_CONSTRAINT")
                .setStart(attributeId)
                .setEnd(identifier)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), edge);
    }
}
