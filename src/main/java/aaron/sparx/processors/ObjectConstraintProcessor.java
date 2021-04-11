package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectConstraintId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.Triple;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAObjectConstraint.*;

public class ObjectConstraintProcessor extends AbstractProcessor{

    public ObjectConstraintProcessor(final String sha1, final LocalDateTime time, final Model model) {
        super(sha1, time, model);
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
                .addProperty("constraint", constraint)
                .addProperty("type", type)
                .addProperty("weight", weight)
                .addProperty("notes", notes)
                .addProperty("status", status)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();

        ObjectId objectIdentifier = new ObjectId(objectId);
        ObjectConstraintId objectConstraintId = new ObjectConstraintId(new Triple<>(objectIdentifier, constraint, type));

        model.addNode(objectConstraintId, node);

        Edge edge = Edge.builder()
                .setType("HAS_CONSTRAINT")
                .setStart(objectIdentifier)
                .setEnd(objectConstraintId)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();
        model.addEdge(new ImplizitRelationId(), edge);
    }
}
