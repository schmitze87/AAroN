package aaron.neo4j.extension;

import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.Map;

public class Util {

    public static String getImportFolder(Transaction t) {
        String path = "/import";
        Result result = t.execute("CALL dbms.listConfig() YIELD name, value \n"
                + "WHERE name = 'dbms.directories.import' \n" +
                "RETURN value;");
        while (result.hasNext()) {
            Map<String, Object> next = result.next();
            Object value = next.get("value");
            path = value != null ? (String) value : "/import";
            return path;
        }
        return path;
    }
}
