package aaron.sparx.model;

public interface EAObjectConstraint {

    String TABLE_NAME = "t_attributeconstraints";

    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> CONSTRAINT = new Column<>("Constraint", String.class);
    Column<String> CONSTRAINT_TYPE = new Column<>("ConstraintType", String.class);
    Column<Double> WEIGHT = new Column<>("Weight", Double.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> STATUS = new Column<>("Status", String.class);
}
