package aaron.sparx.model;

public interface EAObjectProperty {

    String TABLE_NAME = "t_objectProperties";

    Column<Integer> PROPERTY_ID = new Column<>("PropertyID", Integer.class);
    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> PROPERTY = new Column<>("Property", String.class);
    Column<String> VALUE = new Column<>("Value", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
}
