package aaron.sparx.processors;

import aaron.export.TestLogger;
import aaron.logging.Logger;
import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.ConnectorGUID;
import aaron.sparx.identifiers.ObjectGUID;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static aaron.model.PropertyType.STRING;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class XRefProcessorTest {

    private XRefProcessor newProcessor(Model model) {
        Logger logger = new TestLogger(LoggerFactory.getLogger(XRefProcessorTest.class));
        return new XRefProcessor("sha1", LocalDateTime.now(), model, new ImportConext(), logger);
    }

    private Map<String, Object> stereotypeRow(String client, String description) {
        Map<String, Object> row = new HashMap<>();
        row.put("type", "element property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", description);
        return row;
    }

    @Test
    void elementStereotypeWithNullDescriptionDoesNotThrow() {
        Model model = new Model();
        String client = "node-1";
        model.addNode(new ObjectGUID(client), AAroNNode.builder().build());

        XRefProcessor processor = newProcessor(model);

        assertDoesNotThrow(() -> processor.process(stereotypeRow(client, null)));
    }

    @Test
    void connectorStereotypeWithNullDescriptionDoesNotThrow() {
        Model model = new Model();
        String client = "edge-1";
        model.addEdge(new ConnectorGUID(client), AAroNEdge.builder().build());

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = new HashMap<>();
        row.put("type", "connector property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", null);

        assertDoesNotThrow(() -> processor.process(row));
    }

    @Test
    void connectorStereotypeWithMatchingNameSetsFqStereotype() {
        Model model = new Model();
        String client = "edge-2";
        AAroNEdge edge = AAroNEdge.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        model.addEdge(new ConnectorGUID(client), edge);

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = new HashMap<>();
        row.put("type", "connector property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", "@STEREO;Name=Foo;FQName=ns::Foo;@ENDSTEREO;");

        processor.process(row);

        assertEquals("ns::Foo", edge.getProperty(STRING, "fqStereotype"));
    }

    @Test
    void elementStereotypeWithMatchingNameSetsFqStereotype() {
        Model model = new Model();
        String client = "node-2";
        AAroNNode node = AAroNNode.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        model.addNode(new ObjectGUID(client), node);

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = stereotypeRow(
                client, "@STEREO;Name=Foo;FQName=ns::Foo;@ENDSTEREO;");

        processor.process(row);

        assertEquals("ns::Foo", node.getProperty(STRING, "fqStereotype"));
    }

    @Test
    void elementStereotypeWithNonMatchingDescriptionSetsNothing() {
        Model model = new Model();
        String client = "node-3";
        AAroNNode node = AAroNNode.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        model.addNode(new ObjectGUID(client), node);

        XRefProcessor processor = newProcessor(model);

        processor.process(stereotypeRow(client, "kein-stereotyp-token"));

        assertFalse(node.getProperties().containsKey("fqStereotype"));
    }
}
