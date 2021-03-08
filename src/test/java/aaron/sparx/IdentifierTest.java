package aaron.sparx;

import aaron.sparx.identifiers.ObjectGUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class IdentifierTest {

    @Test
    public void test() {
        ObjectGUID objectGUID1 = new ObjectGUID("{02A280D5-40FA-4ca0-9E01-BA9BF3DD8084}");
        ObjectGUID objectGUID2 = new ObjectGUID("{02A280D5-40FA-4ca0-9E01-BA9BF3DD8084}");
        Assertions.assertEquals(objectGUID1, objectGUID2);
    }
}
