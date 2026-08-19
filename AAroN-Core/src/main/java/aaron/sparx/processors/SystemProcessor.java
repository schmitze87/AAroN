package aaron.sparx.processors;

import aaron.logging.Logger;
import aaron.model.*;
import aaron.sparx.identifiers.ProjectGUID;
import aaron.sparx.model.EASystem;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.STRING;

public class SystemProcessor extends AbstractProcessor {

    public SystemProcessor(String sha1, LocalDateTime time, Model model, ImportConext context, Logger logger) {
        super(sha1, time, model, context, logger);
    }

    @Override
    public void process(Map<String, Object> row) {
        String propertyName = EASystem.PROPERTY.value(row);
        String value = EASystem.VALUE.value(row);
        if ("ProjectGUID".equals(propertyName)) {
            AAroNNode node = AAroNNode.builder().build();
            node.addLabel("EA_Project");
            node.addProperty("ProjectGUID", STRING, value);
            ProjectGUID projectGUID = new ProjectGUID(value);
            UniqueNodeIdentifier<java.util.UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
            model.addNode(uniqueNodeIdentifier, node);
            model.addNodeIdentifier(projectGUID, uniqueNodeIdentifier);
            context.setProjectGuid(value);
        }
    }
}
