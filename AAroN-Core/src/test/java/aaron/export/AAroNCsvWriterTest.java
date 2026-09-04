package aaron.export;

import aaron.logging.Logger;
import aaron.model.Converter;
import aaron.model.Model;
import aaron.sparx.SparxFirebirdConverter;
import aaron.sparx.SparxJETConverter;
import aaron.sparx.SparxSQLiteConverter;
import aaron.sparx.config.Config;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class AAroNCsvWriterTest {

    @Test
    void testEAP() throws IOException {
        Logger logger = new TestLogger(LoggerFactory.getLogger(AAroNCsvWriterTest.class));
        URL resource = ClassLoader.getSystemResource("DIV-Hausmesse.eap");
        File file = new File(resource.getFile());
        Map<String, Object> config = new HashMap<>();
        config.put("taggedValues", "AS_PROPERTY");
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        Converter converter = new SparxJETConverter(model, Config.createFromMap(config), file, logger);
        model = converter.convert();
        File nodesFile = new File("nodes.csv");
        File edgesFile = new File("edges.csv");
        System.out.println(nodesFile.getAbsoluteFile());
        AAroNCsvWriter writer = new AAroNCsvWriter();
        writer.write(model, nodesFile, edgesFile, false);
        model.close();
    }

    @Test
    void testQEA() throws IOException {
        Logger logger = new TestLogger(LoggerFactory.getLogger(AAroNCsvWriterTest.class));
        URL resource = ClassLoader.getSystemResource("DIV-Hausmesse.qea");
        File file = new File(resource.getFile());
        Map<String, Object> config = new HashMap<>();
        config.put("taggedValues", "AS_PROPERTY");
        Model.Builder modelBuilder = new Model.Builder();
        modelBuilder.workingDir(new File("."));
        Model model = modelBuilder.build();
        Converter converter = new SparxSQLiteConverter(model, Config.createFromMap(config), file, logger);
        model = converter.convert();
        File nodesFile = new File("nodes.csv");
        File edgesFile = new File("edges.csv");
        System.out.println(nodesFile.getAbsoluteFile());
        AAroNCsvWriter writer = new AAroNCsvWriter();
        writer.write(model, nodesFile, edgesFile, false);
        model.close();
    }

    @Disabled
    @Test
    void testFEAP() throws IOException {
        Logger logger = new TestLogger(LoggerFactory.getLogger(AAroNCsvWriterTest.class));
        URL resource = ClassLoader.getSystemResource("MASC.feap");
        File file = new File(resource.getFile());
        Map<String, Object> config = new HashMap<>();
        config.put("taggedValues", "AS_PROPERTY");
        Model model = new Model.Builder().workingDir(new File(".")).build();
        Converter converter = new SparxFirebirdConverter(model, Config.createFromMap(config), file, logger);
        model = converter.convert();
        File nodesFile = new File("nodes.csv");
        File edgesFile = new File("edges.csv");
        System.out.println(nodesFile.getAbsoluteFile());
        AAroNCsvWriter writer = new AAroNCsvWriter();
        writer.write(model, nodesFile, edgesFile, false);
        model.close();
    }
}
