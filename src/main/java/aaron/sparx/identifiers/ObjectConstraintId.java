package aaron.sparx.identifiers;

import aaron.model.AbstractIdentifier;

public class ObjectConstraintId extends AbstractIdentifier<Triple<ObjectId, String, String>> {

    public ObjectConstraintId(Triple<ObjectId, String, String> id) {
        super(id);
    }
}
