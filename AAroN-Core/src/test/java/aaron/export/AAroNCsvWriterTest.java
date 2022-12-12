package aaron.export;

import aaron.model.Model;
import aaron.sparx.Config;
import aaron.sparx.SparxJETConverter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class AAroNCsvWriterTest {

    @Test
    void test() throws IOException {
        URL resource = ClassLoader.getSystemResource("DIV-Hausmesse.eap");
        File file = new File(resource.getFile());
        Map<String, Object> config = new HashMap<>();
        config.put("taggedValues", "AS_PROPERTY");
        SparxJETConverter converter = new SparxJETConverter(Config.createFromMap(config), file);
        Model model = converter.convert();
        File nodesFile = new File("nodes.csv");
        File edgesFile = new File("edges.csv");
        System.out.println(nodesFile.getAbsoluteFile());
        AAroNCsvWriter.write(model, nodesFile, edgesFile);
    }
}
