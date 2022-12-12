package aaron.sparx.model;

public interface EAConnectorConstraint {

    String TABLE_NAME = "t_connectorconstraint";

    Column<Integer> CONNECTOR_ID = new Column<>("ConnectorID", Integer.class);
    Column<String> CONSTRAINT = new Column<>("Constraint", String.class);
    Column<String> CONSTRAINT_TYPE = new Column<>("ConstraintType", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
}
