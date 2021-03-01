package aaron.sparx.processors;

import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.TaggedValueMode;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.ObjectPropertiesGUID;
import aaron.sparx.identifiers.ObjectPropertiesId;

import java.util.Map;

import static aaron.sparx.model.EAObjectProperty.*;

public class ObjectPropertiesProcessor implements Processor{

    private final Model model;
    private final TaggedValueMode mode;

    public ObjectPropertiesProcessor(final Model model, final TaggedValueMode mode) {
        this.model = model;
        this.mode = mode;
    }

    @Override
    public <U extends Map<String, Object>> void process(final U row) {
        Integer propertyId = PROPERTY_ID.value(row);
        Integer objectId = OBJECT_ID.value(row);
        String property = PROPERTY.value(row);
        String value = VALUE.value(row);
        String notes = NOTES.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));

        ObjectPropertiesId propertyIdentifier = new ObjectPropertiesId(propertyId);
        ObjectPropertiesGUID propertyGUID = new ObjectPropertiesGUID(eaGuid);
        ObjectId objectIdentifier = new ObjectId(objectId);

        TaggedValueHelper.process(model, mode, property, "<memo>".equals(value) ? notes : value, propertyGUID, objectIdentifier);
    }
}
