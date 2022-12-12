package aaron.sparx.model;

public interface EAOperationParams {

    String TABLE_NAME = "t_operation";

    Column<Integer> OPERATION_ID = new Column<>("OperationID", Integer.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> TYPE = new Column<>("Type", String.class);
    Column<String> DEFAULT = new Column<>("Default", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<Integer> POS = new Column<>("Pos", Integer.class);
    Column<Boolean> CONST = new Column<>("Const", Boolean.class);
    Column<String> STYLE = new Column<>("Style", String.class);
    Column<String> KIND = new Column<>("Kind", String.class);
    Column<String> CLASSIFIER = new Column<>("Classifier", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<String> STYLE_EX = new Column<>("StyleEx", String.class);
}
