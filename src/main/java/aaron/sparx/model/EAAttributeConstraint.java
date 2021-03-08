package aaron.sparx.model;

public interface EAAttributeConstraint {

    String TABLE_NAME = "t_attributeconstraints";

    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> CONSTRAINT = new Column<>("Constraint", String.class);
    Column<String> ATT_NAME = new Column<>("AttName", String.class);
    Column<String> TYPE = new Column<>("Type", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<Integer> ID = new Column<>("ID", Integer.class);
}
