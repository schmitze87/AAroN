package aaron.sparx.identifiers;

import aaron.model.AbstractIdentifier;

public class ConnectorConstraintId extends AbstractIdentifier<Tuple<ConnectorId, String>> {

    public ConnectorConstraintId(Tuple<ConnectorId, String> id) {
        super(id);
    }
}
