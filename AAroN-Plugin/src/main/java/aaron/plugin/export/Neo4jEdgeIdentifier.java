package aaron.plugin.export;

import aaron.model.AbstractIdentifier;
import aaron.model.UniqueEdgeIdentifier;

public class Neo4jEdgeIdentifier extends AbstractIdentifier<String> implements UniqueEdgeIdentifier<String> {

    public Neo4jEdgeIdentifier(String id) {
        super(id);
    }
}
