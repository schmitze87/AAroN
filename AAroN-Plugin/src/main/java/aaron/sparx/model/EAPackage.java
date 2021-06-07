package aaron.sparx.model;

import java.time.LocalDateTime;

public interface EAPackage extends Table{

    String TABLE_NAME = "t_package";

    Column<Integer> PACKAGE_ID = new Column<>("Package_ID", Integer.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<Integer> PARENT_ID = new Column<>("Parent_ID", Integer.class);
    Column<LocalDateTime> CREATED_ON = new Column<>("CreatedDate", LocalDateTime.class);
    Column<LocalDateTime> MODIFIED_ON = new Column<>("ModifiedDate", LocalDateTime.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<String> VERSION = new Column<>("Version", String.class);

}
