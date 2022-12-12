package aaron.sparx.model;

public interface EADiagramObject {

    String TABLE_NAME = "t_diagramobjects";

    Column<Integer> DIAGRAM_ID = new Column<>("Diagram_ID", Integer.class);
    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<Integer> RECT_TOP = new Column<>("RectTop", Integer.class);
    Column<Integer> RECT_LEFT = new Column<>("RectLeft", Integer.class);
    Column<Integer> RECT_RIGHT = new Column<>("RectRight", Integer.class);
    Column<Integer> RECT_BOTTOM = new Column<>("RectBottom", Integer.class);
    Column<Integer> SEQUENCE = new Column<>("Sequence", Integer.class);
    Column<String> OBJECT_STYLE = new Column<>("ObjectStyle", String.class);
    Column<Integer> INSTANCE_ID = new Column<>("Instance_ID", Integer.class);
}
