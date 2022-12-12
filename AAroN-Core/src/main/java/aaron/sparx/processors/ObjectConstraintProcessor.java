package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectConstraintId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.Triple;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.*;
import static aaron.sparx.model.EAObjectConstraint.*;

public class ObjectConstraintProcessor extends AbstractProcessor {

    public ObjectConstraintProcessor(final String sha1, final LocalDateTime time, final Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer objectId = OBJECT_ID.value(row);
        String constraint = CONSTRAINT.value(row);
        String type = CONSTRAINT_TYPE.value(row);
        Double weight = WEIGHT.value(row);
        String notes = NOTES.value(row);
        String status = STATUS.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("OBJECT_CONSTRAINT")
                .addLabel("CONSTRAINT")
                .addProperty("constraint", STRING, constraint)
                .addProperty("type", STRING, type)
                .addProperty("weight", DOUBLE, weight)
                .addProperty("notes", STRING, notes)
                .addProperty("status", STRING, status)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();

        ObjectId objectIdentifier = new ObjectId(objectId);
        ObjectConstraintId objectConstraintId = new ObjectConstraintId(new Triple<>(objectIdentifier, constraint, type));

        model.addNode(objectConstraintId, node);

        AAroNEdge edge = AAroNEdge.builder()
                .setType("HAS_CONSTRAINT")
                .setStart(objectIdentifier)
                .setEnd(objectConstraintId)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(), edge);
    }
}
