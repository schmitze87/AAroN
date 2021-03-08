package aaron.sparx.processors;

import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.AttributeTagGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAConnectorTag.*;

public class ConnectorTagProcessor extends  AbstractProcessor{

    private final TaggedValueMode mode;

    public ConnectorTagProcessor(final String sha1, final LocalDateTime time, final Model model) {
        super(sha1, time, model);
        //Tagged Values for connectors need to be added as properties because there is no corresponding node
        this.mode = TaggedValueMode.AS_PROPERTY;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        Integer propertyId = PROPERTY_ID.value(row);
        Integer elementId = ELEMENT_ID.value(row);
        String property = PROPERTY.value(row);
        String value = VALUE.value(row);
        String notes = NOTES.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));

        AttributeId connectorId = new AttributeId(elementId);
        AttributeTagGUID connectorTagGUID = new AttributeTagGUID(eaGuid);

        TaggedValueHelper.process(sha1, time, model, mode, property, "<memo>".equals(value) ? notes : value, connectorTagGUID, connectorId);
    }
}
