package aaron.export;

import java.util.Objects;

class CSVHeader {

    private final String rawName;
    private final String name;
    private final String type;
    private boolean conflictingTypeDefinition = false;

    CSVHeader(final String name, final String type, final boolean parenthesesFix) {
        this.rawName = name;
        this.type = type;
        this.name = createName(name, parenthesesFix);
    }

    static String createName(final String value, final boolean parenthesesFix) {
        if (value == null) {
            return null;
        } else {
            String header = value;
            header = header
                    .trim()
                    .replaceAll("\\s+", " ");
            if (parenthesesFix) {
                return header
                        .replace('(', '[')
                        .replace(')', ']')
                        .replace('{', '[')
                        .replace('}', ']');
            } else {
                return header;
            }
        }
    }

    public String getRawName() {
        return rawName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setConflictingTypeDefinition(boolean conflictingTypeDefinition) {
        this.conflictingTypeDefinition = conflictingTypeDefinition;
    }

    @Override
    public String toString() {
        if (conflictingTypeDefinition) {
            return (name != null ? name : "") + ":string";
        }
        return (name != null ? name : "") + ":" + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSVHeader csvHeader = (CSVHeader) o;
        return Objects.equals(name, csvHeader.name) && Objects.equals(type, csvHeader.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
