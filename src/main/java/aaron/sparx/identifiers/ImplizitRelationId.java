package aaron.sparx.identifiers;

import aaron.model.AbstractIdentifier;

import java.util.UUID;

public class ImplizitRelationId extends AbstractIdentifier<String> {

    public ImplizitRelationId() {
        super(UUID.randomUUID().toString());
    }

    public ImplizitRelationId(String id) {
        super(id);
    }

}
