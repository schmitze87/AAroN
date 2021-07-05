package aaron.util;

import jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
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

    public static String createSHA1(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        return new HexBinaryAdapter().marshal(digest.digest());
    }
}
