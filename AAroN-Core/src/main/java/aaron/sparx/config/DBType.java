package aaron.sparx.config;

public enum DBType {
    MSSQL("MSSQL"),
    MySQL("MSSQL");

    private final String name;

    DBType(final String name) {
        this.name = name;
    }
}
