package aaron.sparx.processors;

import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.OperationId;
import aaron.sparx.identifiers.OperationTagGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAOperationTag.*;

public class OperationTagProcessor extends AbstractProcessor {

    private final TaggedValueMode mode;

    public OperationTagProcessor(final String sha1, final LocalDateTime time, final Model model, final ImportConext context, final TaggedValueMode mode) {
        super(sha1, time, model, context);
        this.mode = mode;
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer propertyId = PROPERTY_ID.value(row);
        Integer elementId = ELEMENT_ID.value(row);
        String property = PROPERTY.value(row);
        String value = VALUE.value(row);
        String notes = NOTES.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));

        OperationId operationId = new OperationId(elementId);
        OperationTagGUID operationTagGUID = new OperationTagGUID(eaGuid);

        TaggedValueHelper.process(sha1, time, model, mode, property, "<memo>".equals(value) ? notes : value, operationTagGUID, operationId);
    }
}
