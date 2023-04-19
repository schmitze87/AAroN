package aaron.util;

import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;

public class Util {

    private Util() {

    }

    public static String getImportFolder(Transaction t) {
        String path = "/import";
        Result result = t.execute("""
CALL dbms.listConfig() YIELD name, value
WHERE name = 'server.directories.import'
RETURN value;""");
        if (result.hasNext()) {
            Map<String, Object> next = result.next();
            Object value = next.get("value");
            path = value != null ? (String) value : "/import";
            return path;
        }
        return path;
    }

    public static String createSHA1(String connection, Instant timestamp) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(connection.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(timestamp.getEpochSecond());
        dos.flush();
        digest.update(bos.toByteArray());
        return HexFormat.of().formatHex(digest.digest());
    }

    public static String createSHA1(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        return getCRCString(file, digest);
    }

    public static String createSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return getCRCString(file, digest);
    }

    private static String getCRCString(File file, MessageDigest digest) throws IOException {
        try (InputStream fis = new FileInputStream(file)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }
}
