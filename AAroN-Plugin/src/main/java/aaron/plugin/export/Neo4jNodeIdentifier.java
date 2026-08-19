package aaron.plugin.export;

import aaron.model.AbstractIdentifier;
import aaron.model.UniqueNodeIdentifier;

public class Neo4jNodeIdentifier extends AbstractIdentifier<String> implements UniqueNodeIdentifier<String> {

    public Neo4jNodeIdentifier(String id) {
        super(id);
    }
}
