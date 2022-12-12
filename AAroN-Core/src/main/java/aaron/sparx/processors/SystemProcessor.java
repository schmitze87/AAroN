package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.ProjectGUID;
import aaron.sparx.model.EASystem;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.STRING;

public class SystemProcessor extends AbstractProcessor {

    public SystemProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(Map<String, Object> row) {
        String propertyName = EASystem.PROPERTY.value(row);
        String value = EASystem.VALUE.value(row);
        if ("ProjectGUID".equals(propertyName)) {
            AAroNNode node = AAroNNode.builder().build();
            node.addLabel("EA_Project");
            node.addProperty("ProjectGUID", STRING, value);
            model.addNode(new ProjectGUID(value), node);
            context.setProjectGuid(value);
        }
    }
}
