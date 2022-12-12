package aaron.sparx.model;

public interface EASystem extends Table {

    String TABLE_NAME = "usys_system";

    Column<String> PROPERTY = new Column<>("Property", String.class);
    Column<String> VALUE = new Column<>("Value", String.class);
}
