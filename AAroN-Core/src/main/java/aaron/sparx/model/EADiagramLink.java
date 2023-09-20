package aaron.sparx.model;

public interface EADiagramLink {

    String TABLE_NAME = "t_diagramlinks";

    Column<Integer> DIAGRAMID = new Column<>("DiagramID", Integer.class);
    Column<Integer> ConnectorID = new Column<>("ConnectorID", Integer.class);
    Column<String> GEOMETRY = new Column<>("Geometry", String.class);
    Column<String> STYLE = new Column<>("Style", String.class);
    Column<Boolean> HIDDEN = new Column<>("Hidden", Boolean.class);
    Column<String> PATH = new Column<>("Path", String.class);
    Column<Integer> INSTANCE_ID = new Column<>("Instance_ID", Integer.class);
}
