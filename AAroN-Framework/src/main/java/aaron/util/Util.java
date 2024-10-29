package aaron.util;

import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;

public class Util {

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String getImportFolder(Transaction t) {
        String path = "/import";
        Result result = t.execute("CALL dbms.listConfig() YIELD name, value \n"
                + "WHERE name = 'dbms.directories.import' OR name = 'server.directories.import' \n" +
                "RETURN value;");
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
        return encodeHexString(digest.digest()).toLowerCase();
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
        return encodeHexString(digest.digest()).toLowerCase();
    }

    public static String createSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }

        return encodeHexString(digest.digest()).toLowerCase();
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to be encoded
     * @return A string of hex characters [0-9A-F]
     */
    private static String encodeHexString( byte[] bytes )
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String( hexChars );
    }

    /**
     * Converts a hexadecimal string to a byte array
     *
     * @param hexString A string of hexadecimal characters [0-9A-Fa-f] to decode
     * @return Decoded bytes, or null if the {@param hexString} is not valid
     */
    private static byte[] decodeHexString( String hexString )
    {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for ( int i = 0, j = 0; i < len; i += 2, j++ )
        {
            int highByte = Character.digit( hexString.charAt( i ), 16 ) << 4;
            int lowByte = Character.digit( hexString.charAt( i + 1 ), 16 );
            if ( highByte < 0 || lowByte < 0 )
            {
                return null;
            }
            data[j] = (byte) ( highByte + lowByte );
        }
        return data;
    }
}
