package aaron.plugin.export;

import aaron.model.AbstractIdentifier;

public class Neo4jNodeIdentifier extends AbstractIdentifier<Long> {

    public Neo4jNodeIdentifier(Long id) {
        super(id);
    }
}
