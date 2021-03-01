package aaron.sparx.model;

public interface EAAttribute {

    String TABLE_NAME = "t_attribute";

    Column<Integer> OBJECT_ID = new Column<>("Object_ID", Integer.class);
    Column<String> NAME = new Column<>("Name", String.class);
    Column<String> SCOPE = new Column<>("Scope", String.class);
    Column<String> STEREOTYPE = new Column<>("Stereotype", String.class);
    Column<String> CONTAINMENT = new Column<>("Containment", String.class);
    Column<Boolean> IS_STATIC = new Column<>("IsStatic", Boolean.class);
    Column<Boolean> IS_COLLECTION = new Column<>("IsCollection", Boolean.class);
    Column<Boolean> IS_ORDERED = new Column<>("IsOrdered", Boolean.class);
    Column<Boolean> ALLOW_DUPLICATES = new Column<>("AllowDuplicates", Boolean.class);
    Column<String> LOWER_BOUND = new Column<>("LowerBound", String.class);
    Column<String> UPPER_BOUND = new Column<>("UpperBound", String.class);
    Column<String> CONTAINER = new Column<>("Container", String.class);
    Column<String> NOTES = new Column<>("Notes", String.class);
    Column<String> DERIVED = new Column<>("Derived", String.class);
    Column<Integer> ID = new Column<>("ID", Integer.class);
    Column<Integer> POS = new Column<>("Pos", Integer.class);
    Column<String> GEN_OPTIONS = new Column<>("GenOption", String.class);
    Column<Integer> LENGTH = new Column<>("Length", Integer.class);
    Column<Integer> PRECISION = new Column<>("Precision", Integer.class);
    Column<Integer> SCALE = new Column<>("Scale", Integer.class);
    Column<Integer> CONST = new Column<>("Const", Integer.class);
    Column<String> STYLE = new Column<>("Style", String.class);
    Column<String> CLASSIFIER = new Column<>("Classifier", String.class);
    Column<String> DEFAULT = new Column<>("Default", String.class);
    Column<String> TYPE = new Column<>("Type", String.class);
    Column<String> EA_GUID = new Column<>("ea_guid", String.class);
    Column<String> STYLE_EX = new Column<>("StyleEx", String.class);
}
