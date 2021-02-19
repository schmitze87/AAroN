package aaron.sparx.model;

import java.time.LocalDateTime;

public interface EADiagram extends Table{

    String TABLE_NAME = "t_diagram";

    Column<Integer> DIAGRAM_ID = new Column<>("Diagram_ID", Integer.class);
    Column<Integer> PACKAGE_ID = new Column<>("Package_ID", Integer.class);
    Column<Integer> PARENT_ID = new Column<>("ParentID", Integer.class);
    Column<String> DIAGRAM_TYPE = new Column<>("Diagram_Type", String.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> VERSION = new Column<>("Version", String.class);
    Column<String> AUTHOR = new Column<>("Author", String.class);
    Column<Integer> SHOW_DETAILS = new Column<>("ShowDetails", Integer.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> STEREOTYPE = new Column<>("Stereotype", String.class);
    Column<Boolean> ATT_PUB = new Column<>("AttPub", Boolean.class);
    Column<Boolean> ATT_PRI = new Column<>("AttPri", Boolean.class);
    Column<Boolean> ATT_PRO = new Column<>("AttPro", Boolean.class);
    Column<String> ORIENTATION = new Column<>("Orientation", String.class);
    Column<Integer> CX = new Column<>("cx", Integer.class);
    Column<Integer> CY = new Column<>("cy", Integer.class);
    Column<Integer> SCALE = new Column<>("Scale", Integer.class);
    Column<LocalDateTime> CREATED_DATE = new Column<>("CreatedDate", LocalDateTime.class);
    Column<LocalDateTime> MODIFIED_DATE = new Column<>("ModifiedDate", LocalDateTime.class);
    Column<String> HTML_PATH = new Column<>("HTMLPath", String.class);
    Column<Boolean> SHOW_FOREIGN = new Column<>("ShowForeign", Boolean.class);
    Column<Boolean> SHOW_BORDER = new Column<>("ShowBorder", Boolean.class);
    Column<Boolean> SHOW_PACKAGE_CONTENTS = new Column<>("ShowPackageContents", Boolean.class);
    Column<String> PDATA = new Column<>("PDATA", String.class);
    Column<Boolean> LOCKED = new Column<>("Locked", Boolean.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<Integer> TPOS = new Column<>("TPos", Integer.class);
    Column<String> SWIMLANES = new Column<>("Swimlanes", String.class);
    Column<String> STYLE_EX = new Column<>("StyleEx", String.class);

}
