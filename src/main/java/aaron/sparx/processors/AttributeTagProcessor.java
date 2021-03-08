package aaron.sparx.processors;

import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.AttributeTagGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAAttributeTag.*;

public class AttributeTagProcessor extends AbstractProcessor{

    private final TaggedValueMode mode;

    public AttributeTagProcessor(String sha1, LocalDateTime time, Model model, TaggedValueMode mode) {
        super(sha1, time, model);
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

        AttributeId attributeId = new AttributeId(elementId);
        AttributeTagGUID attributeTagGUID = new AttributeTagGUID(eaGuid);

        TaggedValueHelper.process(sha1, time, model, mode, property, "<memo>".equals(value) ? notes : value, attributeTagGUID, attributeId);
    }
}
