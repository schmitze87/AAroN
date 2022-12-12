package aaron.sparx.model;

public interface EAXref extends Table {

    String TABLE_NAME = "t_xref";

    Column<String> XREF_ID = new Column<>("XrefID", String.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> TYPE = new Column<>("Type", String.class);
    Column<String> VISIBILITY = new Column<>("Visibility", String.class);
    Column<String> NAMESPACE = new Column<>("Namespace", String.class);
    Column<String> REQUIREMENT = new Column<>("Requirement", String.class);
    Column<String> CONSTRAINT = new Column<>("Constraint", String.class);
    Column<String> BEHAVIOR = new Column<>("Behavior", String.class);
    Column<String> PARTITION = new Column<>("Partition", String.class);
    Column<String> DESCRIPTION = new Column<>("Description", String.class);
    Column<String> CLIENT = new Column<>("Client", String.class);
    Column<String> SUPPLIER = new Column<>("Supplier", String.class);
    Column<String> LINK = new Column<>("Link", String.class);
}
