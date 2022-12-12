package aaron.sparx.processors;

import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.ObjectPropertiesGUID;
import aaron.sparx.identifiers.ObjectPropertiesId;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAObjectProperty.*;

public class ObjectPropertiesProcessor extends AbstractProcessor {

    private final TaggedValueMode mode;

    public ObjectPropertiesProcessor(final String sha1, final LocalDateTime time, final Model model, final ImportConext context, final TaggedValueMode mode) {
        super(sha1, time, model, context);
        this.mode = mode;
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer propertyId = PROPERTY_ID.value(row);
        Integer objectId = OBJECT_ID.value(row);
        String property = PROPERTY.value(row);
        String value = VALUE.value(row);
        String notes = NOTES.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));

        ObjectPropertiesId propertyIdentifier = new ObjectPropertiesId(propertyId);
        ObjectPropertiesGUID propertyGUID = new ObjectPropertiesGUID(eaGuid);
        ObjectId objectIdentifier = new ObjectId(objectId);

        TaggedValueHelper.process(sha1, time, model, mode, property, "<memo>".equals(value) ? notes : value, propertyGUID, objectIdentifier);
    }
}
