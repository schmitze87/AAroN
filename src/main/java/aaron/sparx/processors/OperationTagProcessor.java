package aaron.sparx.processors;

import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.OperationId;
import aaron.sparx.identifiers.OperationTagGUID;

import java.util.Map;

import static aaron.sparx.model.EAOperationTag.*;

public class OperationTagProcessor implements Processor{

    private final Model model;
    private final TaggedValueMode mode;

    public OperationTagProcessor(final Model model, final TaggedValueMode mode) {
        this.model = model;
        this.mode = mode;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        Integer propertyId = PROPERTY_ID.value(row);
        Integer elementId = ELEMENT_ID.value(row);
        String property = PROPERTY.value(row);
        String value = VALUE.value(row);
        String notes = NOTES.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));

        OperationId operationId = new OperationId(elementId);
        OperationTagGUID operationTagGUID = new OperationTagGUID(eaGuid);

        TaggedValueHelper.process(model, mode, property, "<memo>".equals(value) ? notes : value, operationTagGUID, operationId);
    }
}
