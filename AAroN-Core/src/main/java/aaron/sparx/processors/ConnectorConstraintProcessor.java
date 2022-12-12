package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.ConnectorId;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.STRING_ARRAY;
import static aaron.sparx.model.EAConnectorConstraint.*;

public class ConnectorConstraintProcessor extends AbstractProcessor {

    public ConnectorConstraintProcessor(final String sha1, final LocalDateTime time, final Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer connectorId = CONNECTOR_ID.value(row);
        String constraint = CONSTRAINT.value(row);
        String constraintType = CONSTRAINT_TYPE.value(row);
        String notes = NOTES.value(row);

//        AAroNNode node = AAroNNode.builder()
//                .addLabel("CONNECTOR_CONSTRAINT")
//                .addLabel("CONSTRAINT")
//                .addProperty("constraint", constraint)
//                .addProperty("constraintType", constraintType)
//                .addProperty("notes", notes)
//                .addProperty("eapHash", sha1)
//                .addProperty("importedAd", time)
//                .build();

        ConnectorId connectorIdentifier = new ConnectorId(connectorId);
        AAroNEdge connectorEdge = model.getEdge(connectorIdentifier);
        connectorEdge.addProperty("constraint_" + constraint, STRING_ARRAY, new String[]{notes, constraintType});
    }
}
