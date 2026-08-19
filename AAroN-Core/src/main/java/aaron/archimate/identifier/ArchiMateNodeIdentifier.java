package aaron.archimate.identifier;

import aaron.model.AbstractIdentifier;
import aaron.model.UniqueNodeIdentifier;

public class ArchiMateNodeIdentifier extends AbstractIdentifier<String> implements UniqueNodeIdentifier<String> {

    public ArchiMateNodeIdentifier(String id) {
        super(id);
    }
}
