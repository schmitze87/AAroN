package aaron.sparx.model;

public interface EAConnectorTag {

    String TABLE_NAME = "t_connectortag";

    Column<Integer> PROPERTY_ID = new Column<>("PropertyID", Integer.class);
    Column<Integer> ELEMENT_ID = new Column<>("ElementID", Integer.class);
    Column<String> PROPERTY = new Column<>("Property", String.class);
    Column<String> VALUE = new Column<>("VALUE", String.class);
    Column<String> NOTES = new Column<>("NOTES", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
}
