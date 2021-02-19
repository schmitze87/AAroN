package aaron.sparx.model;

import java.time.LocalDateTime;

public interface EAObject extends Table{

    String TABLE_NAME = "t_object";

    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> OBJECT_TYPE = new Column<>("Object_Type", String.class);
    Column<Integer> DIAGRAM_ID = new Column<>("Diagram_ID", Integer.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> ALIAS = new Column<>("Alias", String.class);
    Column<String> AUTHOR = new Column<>("Author", String.class);
    Column<String> VERSION = new Column<>("Version", String.class);
    Column<String> NOTE = new Column<>("Note", String.class);
    Column<Integer> PACKAGE_ID = new Column<>("Package_ID", Integer.class);
    Column<String> STEREOTYPE = new Column<>("Stereotype", String.class);
    Column<Integer> NTYPE= new Column<>("NType", Integer.class);
    Column<String> COMPLEXITY = new Column<>("Complexity", String.class);
    Column<Integer> EFFORT = new Column<>("Effort", Integer.class);
    Column<String> STYLE = new Column<>("Style", String.class);
    Column<Integer> BACKCOLOR = new Column<>("Backcolor", Integer.class);
    Column<Integer> BORDER_STYLE = new Column<>("BorderStyle", Integer.class);
    Column<Integer> BORDER_WIDTH = new Column<>("BorderWidth", Integer.class);
    Column<Integer> FONT_COLOR = new Column<>("Fontcolor", Integer.class);
    Column<Integer> BORDER_COLOR = new Column<>("Bordercolor", Integer.class);
    Column<LocalDateTime> CREATED_DATE = new Column<>("CreatedDate", LocalDateTime.class);
    Column<LocalDateTime> MODIFIED_DATE = new Column<>("ModifiedDate", LocalDateTime.class);
    Column<String> STATUS = new Column<>("Status", String.class);
    Column<String> ABSTRACT = new Column<>("Abstract", String.class);
    Column<Integer> TAGGED = new Column<>("Tagged", Integer.class);
    Column<String> PDATA1 = new Column<>("PDATA1", String.class);
    Column<String> PDATA2 = new Column<>("PDATA2", String.class);
    Column<String> PDATA3 = new Column<>("PDATA3", String.class);
    Column<String> PDATA4 = new Column<>("PDATA4", String.class);
    Column<String> PDATA5 = new Column<>("PDATA5", String.class);
    Column<String> CONCURRENCY = new Column<>("Concurrency", String.class);
    Column<String> VISIBILITY = new Column<>("Visibility", String.class);
    Column<String> PERSISTENCE = new Column<>("Persistence", String.class);
    Column<String> CARDINALITY = new Column<>("Cardinality", String.class);
    Column<String> GEN_TYPE = new Column<>("GenType", String.class);
    Column<String> GEN_FILE = new Column<>("GenFile", String.class);
    Column<String> HEADER1 = new Column<>("Header1", String.class);
    Column<String> HEADER2 = new Column<>("Header2", String.class);
    Column<String> PHASE = new Column<>("Phase", String.class);
    Column<String> SCOPE = new Column<>("Scope", String.class);
    Column<String> GEN_OPTION = new Column<>("GenOption", String.class);
    Column<String> GEN_LINKS = new Column<>("GenLinks", String.class);
    Column<Integer> CLASSIFIER = new Column<>("Classifier", Integer.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<Integer> PARENT_ID = new Column<>("ParentID", Integer.class);
    Column<String> RUN_STATE = new Column<>("RunState", String.class);
    Column<String> CLASSIFIER_GUID = new Column<>("Classifier_guid", String.class);
    Column<Integer> TPOS = new Column<>("TPos", Integer.class);
    Column<Boolean> IS_ROOT = new Column<>("IsRoot", Boolean.class);
    Column<Boolean> IS_LEAF = new Column<>("IsLeaf", Boolean.class);
    Column<Boolean> IS_SPEC = new Column<>("IsSpec", Boolean.class);
    Column<Boolean> IS_ACTIVE = new Column<>("IsActive", Boolean.class);
    Column<String> STATE_FLAGS = new Column<>("StateFlags", String.class);
    Column<String> PACKAGE_FLAGS = new Column<>("PackageFlags", String.class);
    Column<String> MULTIPLICITY = new Column<>("Multiplicity", String.class);
    Column<String> STYLE_EX = new Column<>("StyleEx", String.class);
    Column<String> ACTION_FLAGS = new Column<>("ActionFlags", String.class);
    Column<String> EVENT_FLAGS = new Column<>("EventFlags", String.class);

}
