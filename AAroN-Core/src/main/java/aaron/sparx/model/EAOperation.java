package aaron.sparx.model;

public interface EAOperation {

    String TABLE_NAME = "t_operation";

    Column<Integer> OPERATION_ID = new Column<>("OperationID", Integer.class);
    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> SCOPE = new Column<>("Scope", String.class);
    Column<String> TYPE = new Column<>("Type", String.class);
    Column<Boolean> RETURN_ARRAY = new Column<>("ReturnArray", Boolean.class);
    Column<String> STEREOTYPE = new Column<>("Stereotype", String.class);
    Column<Boolean> IS_STATIC = new Column<>("IsStatic", Boolean.class);
    Column<String> CONCURRENCY = new Column<>("Concurrency", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> BEHAVIOUR = new Column<>("Behaviour", String.class);
    Column<Boolean> ABSTRACT = new Column<>("Abstract", Boolean.class);
    Column<String> GEN_OPTION = new Column<>("GenOption", String.class);
    Column<String> SYNCHRONIZED = new Column<>("Synchronized", String.class);
    Column<Integer> POS = new Column<>("Pos", Integer.class);
    Column<Boolean> CONST = new Column<>("Const", Boolean.class);
    Column<String> STYLE = new Column<>("Style", String.class);
    Column<Boolean> PURE = new Column<>("Pure", Boolean.class);
    Column<String> THROWS = new Column<>("Throws", String.class);
    Column<String> CLASSIFIER = new Column<>("Classifier", String.class);
    Column<String> CODE = new Column<>("Code", String.class);
    Column<Boolean> IS_ROOT = new Column<>("IsRoot", Boolean.class);
    Column<Boolean> IS_LEAF = new Column<>("IsLeaf", Boolean.class);
    Column<Boolean> IS_QUERY = new Column<>("IsQuery", Boolean.class);
    Column<String> STATE_FLAGS = new Column<>("StateFlags", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<String> STYLE_EX = new Column<>("StyleEx", String.class);
}
